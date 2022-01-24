package com.coretex.build.services.impl.mysql

import com.coretex.build.services.DbService
import com.coretex.build.services.QueryFactory
import com.mysql.cj.conf.ConnectionUrl
import com.mysql.cj.conf.ConnectionUrlParser
import com.mysql.cj.jdbc.MysqlDataSource
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import javax.sql.DataSource
import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.ResultSet
import java.sql.Statement

class MySqlDbService extends DbService {

    private Logger LOG = Logging.getLogger(MySqlDbService)

    private QueryFactory queryFactory

    MySqlDbService(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    DataSource createDataSource(String connectionUrl, String username, String password, boolean createIfNotExist) {

        MysqlDataSource dataSource = new MysqlDataSource()
        dataSource.setUrl(connectionUrl)
        dataSource.setUser(username)
        dataSource.setPassword(password)

        verifyIfDbExist(connectionUrl, dataSource, createIfNotExist)
        return dataSource
    }


    void verifyIfDbExist(String connectionUrl,  MysqlDataSource dataSource, boolean createIfNotExist) {
        def urlInstance = ConnectionUrl.getConnectionUrlInstance(connectionUrl, null)
        def dbUrl = connectionUrl.replace(urlInstance.database, "")
        def exist = false
        def databaseName = urlInstance.database
        dataSource.setUrl(dbUrl)
        Connection connection = dataSource.getConnection()

        DatabaseMetaData metadata = connection.getMetaData()
        ResultSet result = metadata.getCatalogs()

        while (result.next()) {
            String aDBName = result.getString(1);
            if (!exist && aDBName.equalsIgnoreCase(databaseName)) {
                exist = true
            }
        }

        dataSource.getConnection().withCloseable { c ->
            if (!exist && createIfNotExist) {
                LOG.lifecycle("CREATE DATABASE [${databaseName}]")
                Statement newDbStatement = c.createStatement()
                newDbStatement.executeUpdate(queryFactory.dbQueryFactory().creteDatabase(databaseName))
                newDbStatement.close()
            }
            if (!exist && !createIfNotExist) {
                throw new IllegalAccessException("Database [${databaseName}] is not exist. Please execute [ ./gradlew initialize ]")
            }
        }

        dataSource.setUrl(connectionUrl)

    }
}
