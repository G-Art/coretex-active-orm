package com.coretex.build.services.impl.postgres

import com.coretex.build.services.DbQueryFactory
import com.coretex.build.services.DbService
import com.coretex.build.services.QueryFactory
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.postgresql.ds.PGSimpleDataSource

import javax.sql.DataSource
import java.sql.Statement

class PostgresqlDbService extends DbService{

    private Logger LOG = Logging.getLogger(PostgresqlDbService)

    private QueryFactory queryFactory

    PostgresqlDbService(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    DataSource createDataSource(String connectionUrl, String username, String password, boolean createIfNotExist) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource()
        dataSource.setUrl(connectionUrl)
        dataSource.setUser(username)
        dataSource.setPassword(password)

        verifyIfDbExist(dataSource, createIfNotExist)
        return dataSource
    }


    void verifyIfDbExist(PGSimpleDataSource dataSource,  boolean createIfNotExist){
        StringBuilder noTableUrl = new StringBuilder(100)
        noTableUrl.append("jdbc:postgresql://")
        noTableUrl.append(dataSource.getServerNames()[0])
        if (dataSource.getPortNumbers() != null || dataSource.getPortNumbers().length != 0) {
            noTableUrl.append(":").append(dataSource.getPortNumbers()[0])
        }
        noTableUrl.append("/")
        def exist = false
        def databaseName = dataSource.databaseName
        dataSource.databaseName = ""
        dataSource.getConnection().withCloseable {c ->
            Statement statement = c.createStatement()
            def rs = statement.executeQuery(queryFactory.dbQueryFactory().showDatabaseByName(databaseName))

            while (rs.next()) {
                exist = rs.getInt(1)>0
            }
            rs.close()
            statement.close()

            if(!exist && createIfNotExist){
                LOG.lifecycle("CREATE DATABASE [${databaseName}]")
                Statement newDbStatement = c.createStatement()
                newDbStatement.executeUpdate(queryFactory.dbQueryFactory().creteDatabase(databaseName))
                newDbStatement.close()
            }
            if(!exist && !createIfNotExist){
                throw new IllegalAccessException("Database [${databaseName}] is not exist. Please execute [ ./gradlew initialize ]")
            }
        }

        dataSource.databaseName = databaseName

    }
}
