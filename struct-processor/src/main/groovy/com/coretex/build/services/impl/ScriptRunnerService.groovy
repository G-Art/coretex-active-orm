package com.coretex.build.services.impl

import com.coretex.Constants
import com.coretex.build.context.CoretexPluginContext
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import java.sql.Connection
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

class ScriptRunnerService {
    private Logger LOG = Logging.getLogger(ScriptRunnerService)

    private static final String DEFAULT_DELIMITER = ";"
    private static final Pattern SOURCE_COMMAND = Pattern.compile('^\\s*SOURCE\\s+(.*?)\\s*$', Pattern.CASE_INSENSITIVE)

    /**
     * regex to detect delimiter.
     * ignores spaces, allows delimiter in comment, allows an equals-sign
     */
    public static final Pattern delimP = Pattern.compile('^\\s*(--)?\\s*delimiter\\s*=?\\s*([^\\s]+)+\\s*.*$', Pattern.CASE_INSENSITIVE)

    private final boolean stopOnError
    private final boolean autoCommit

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private PrintWriter logWriter = null
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private PrintWriter errorLogWriter = null

    private String delimiter = DEFAULT_DELIMITER
    private boolean fullLineDelimiter = false

    CoretexPluginContext context = CoretexPluginContext.instance

    ScriptRunnerService() {
        this(true, true)
    }
/**
 * Default constructor
 */
    ScriptRunnerService(boolean autoCommit,
                        boolean stopOnError) {
        this.autoCommit = autoCommit
        this.stopOnError = stopOnError
        File logFile = new File("${context.getBuildProperty(Constants.PROJECT).projectDir.absolutePath}/tmp/logs/create_db.log")
        File errorLogFile = new File("${context.getBuildProperty(Constants.PROJECT).projectDir.absolutePath}/tmp/logs/create_db_error.log")
        try {
            if (logFile.exists()) {
                logWriter = new PrintWriter(new FileWriter(logFile, true))
            } else {
                logWriter = new PrintWriter(new FileWriter(logFile, false))
            }
        } catch(IOException e){
            LOG.error("Unable to access or create the db_create log")
        }
        try {
            if (errorLogFile.exists()) {
                errorLogWriter = new PrintWriter(new FileWriter(errorLogFile, true))
            } else {
                errorLogWriter = new PrintWriter(new FileWriter(errorLogFile, false))
            }
        } catch(IOException e){
            LOG.error("Unable to access or create the db_create error log")
        }
    }

    void setDelimiter(String delimiter, boolean fullLineDelimiter) {
        this.delimiter = delimiter
        this.fullLineDelimiter = fullLineDelimiter
    }
    /**
     * Set the current working directory.  Source commands will be relative to this.
     */
    void setUserDirectory(String userDirectory) {
        this.userDirectory = userDirectory
    }


    void runScript(Connection conn, File file) throws IOException, SQLException {
        String timeStamp = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss").format(new Date())
        println(conn, "\n-------\n" + timeStamp + "\n-------\n")
        printlnError(conn, "\n-------\n" + timeStamp + "\n-------\n")
        try {
            boolean originalAutoCommit = conn.getAutoCommit()
            try {
                if (originalAutoCommit != this.autoCommit) {
                    conn.setAutoCommit(this.autoCommit)
                }
                runScript(conn, new BufferedReader(new FileReader(file)))
            } finally {
                conn.setAutoCommit(originalAutoCommit)
            }
        } catch (IOException | SQLException e) {
            throw e
        } catch (Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e)
        }
    }
    /**
     * Runs an SQL script (read in using the Reader parameter) using the
     * connection passed in
     *
     * @param conn - the connection to use for the script
     * @param reader - the source of the script
     * @throws SQLException if any SQL errors occur
     * @throws IOException if there is an error reading from the Reader
     */
    void runScript(Connection conn, Reader reader) throws IOException,
            SQLException {
        StringBuffer command = null
        try {
            LineNumberReader lineReader = new LineNumberReader(reader)
            String line
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer()
                }
                String trimmedLine = line.trim()
                final Matcher delimMatch = delimP.matcher(trimmedLine)
                if (trimmedLine.length() < 1
                        || trimmedLine.startsWith("//")) {
                    // Do nothing
                } else if (delimMatch.matches()) {
                    setDelimiter(delimMatch.group(2), false)
                } else if (trimmedLine.startsWith("--")) {
                    println(conn, trimmedLine)
                } else if (trimmedLine.length() < 1
                        || trimmedLine.startsWith("--")) {
                    // Do nothing
                } else if (!fullLineDelimiter
                        && trimmedLine.endsWith(getDelimiter())
                        || fullLineDelimiter
                        && trimmedLine.equals(getDelimiter())) {
                    command.append(line.substring(0, line
                            .lastIndexOf(getDelimiter())))
                    command.append(" ")
                    this.execCommand(conn, command, lineReader)
                    command = null
                } else {
                    command.append(line)
                    command.append("\n")
                }
            }
            if (command != null) {
                this.execCommand(conn, command, lineReader)
            }
            if (!autoCommit) {
                conn.commit()
            }
        }
        catch (IOException e) {
            throw new IOException(String.format("Error executing '%s': %s", command, e.getMessage()), e)
        } finally {
            if (!autoCommit) {
                conn.rollback()
            }
            flush()
        }
    }

    private void execCommand(Connection conn, StringBuffer command,
                             LineNumberReader lineReader) throws IOException, SQLException {

        if (command.length() == 0) {
            return
        }

        Matcher sourceCommandMatcher = SOURCE_COMMAND.matcher(command)
        if (sourceCommandMatcher.matches()) {
            this.runScriptFile(conn, sourceCommandMatcher.group(1))
            return
        }

        this.execSqlCommand(conn, command, lineReader)
    }

    private void runScriptFile(Connection conn, String filepath) throws IOException, SQLException {
        File file = new File(filepath)
        this.runScript(conn, new BufferedReader(new FileReader(file)))
    }

    private void execSqlCommand(Connection conn, StringBuffer command,
                                LineNumberReader lineReader) throws SQLException {

        Statement statement = conn.createStatement()

        println(conn, command.toString())

        boolean hasResults = false
        try {
            hasResults = statement.execute(command.toString())
        } catch (SQLException e) {
            final String errText = String.format("Error executing '%s' (line %d): %s",
                    command, lineReader.getLineNumber(), e.getMessage())
            printlnError(conn, errText)
            if (stopOnError) {
                throw new SQLException(errText, e)
            }
        }

        if (autoCommit && !conn.getAutoCommit()) {
            conn.commit()
        }

        ResultSet rs = statement.getResultSet()
        if (hasResults && rs != null) {
            ResultSetMetaData md = rs.getMetaData()
            int cols = md.getColumnCount()
            for (int i = 1; i <= cols; i++) {
                String name = md.getColumnLabel(i)
                print(name + "\t")
            }
            println(conn,"")
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    String value = rs.getString(i)
                    print(value + "\t")
                }
                println(conn, "")
            }
        }

        try {
            statement.close()
        } catch (Exception e) {
            // Ignore to workaround a bug in Jakarta DBCP
        }
    }

    private String getDelimiter() {
        return delimiter
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private void print(Object o) {
        if (logWriter != null) {
            logWriter.print(o)
        }
    }

    private void println(Connection con, Object o) {
        if (logWriter != null) {
            logWriter.println(o)
            LOG.debug(String.valueOf(o))
        }
    }

    private void printlnError(Connection con, Object o) {
        if (errorLogWriter != null) {
            errorLogWriter.println(o)
            LOG.error(String.valueOf(o))
        }
    }

    private void flush() {
        if (logWriter != null) {
            logWriter.flush()
        }
        if (errorLogWriter != null) {
            errorLogWriter.flush()
        }
    }
}
