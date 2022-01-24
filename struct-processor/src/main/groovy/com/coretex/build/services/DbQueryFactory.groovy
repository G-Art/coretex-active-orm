package com.coretex.build.services

interface DbQueryFactory {

    String showDatabaseByName(String databaseName)

    String creteDatabase(String databaseName)

}
