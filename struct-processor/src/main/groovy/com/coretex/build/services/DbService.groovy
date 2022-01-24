package com.coretex.build.services

import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.services.impl.ScriptRunnerService
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import javax.sql.DataSource
import java.sql.Connection
import java.util.function.Function
import java.util.stream.Stream

abstract class DbService {

    private Logger LOG = Logging.getLogger(DbService)

    private ScriptRunnerService scriptRunner = new ScriptRunnerService()
    private String connectionUrl
    private File initScript

    void initDb(String connectionUrl, String username, String password, File initScript) {
        this.connectionUrl = connectionUrl
        this.initScript = initScript

        DataSource ds = createDataSource(connectionUrl, username, password)
        scriptRunner.runScript(ds.getConnection(), initScript)
    }

    void updateDb(String connectionUrl, String username, String password, Function<Connection, Stream<Resolution>> resolutions) {
        this.connectionUrl = connectionUrl
        LOG.lifecycle("Performing system update")
        DataSource ds = createDataSource(connectionUrl, username, password, false)
        ds.getConnection().withCloseable { connection ->
            resolutions.apply(connection).forEach({ re ->
                LOG.lifecycle(re.toString())
                re.commands.forEach({ query ->
                    LOG.lifecycle("Executing query :: [${query.strip()}]")
                    connection.createStatement().withCloseable { statement ->
                        statement.execute(query)
                    }
                })
            })
        }
        LOG.lifecycle("System update has been done")
    }

    DataSource createDataSource(String connectionUrl, String username, String password) {
        this.createDataSource(connectionUrl, username, password, true)
    }

    abstract DataSource createDataSource(String connectionUrl, String username, String password, boolean createIfNotExist)

    String getConnectionUrl() {
        return connectionUrl
    }

    File getInitScript() {
        return initScript
    }
}
