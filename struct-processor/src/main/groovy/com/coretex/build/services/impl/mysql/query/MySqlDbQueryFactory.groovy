package com.coretex.build.services.impl.mysql.query

import com.coretex.build.services.DbQueryFactory
import org.gradle.internal.impldep.org.apache.http.MethodNotSupportedException

class MySqlDbQueryFactory implements DbQueryFactory {


    @Override
    String showDatabaseByName(String databaseName) {
        throw new MethodNotSupportedException('Method [showDatabaseByName] not supported for MySql')
    }

    @Override
    String creteDatabase(String databaseName) {
        return "CREATE DATABASE ${databaseName};"
    }
}
