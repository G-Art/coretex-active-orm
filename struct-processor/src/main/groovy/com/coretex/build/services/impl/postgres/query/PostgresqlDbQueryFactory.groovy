package com.coretex.build.services.impl.postgres.query

import com.coretex.build.services.DbQueryFactory

class PostgresqlDbQueryFactory implements DbQueryFactory {

    @Override
    String showDatabaseByName(String databaseName) {
        return "SELECT count(*) FROM pg_database WHERE datistemplate = false AND datname = '${databaseName}';"
    }

    @Override
    String creteDatabase(String databaseName) {
        return "CREATE DATABASE ${databaseName};"
    }
}
