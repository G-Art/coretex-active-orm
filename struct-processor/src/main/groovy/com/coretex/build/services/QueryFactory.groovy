package com.coretex.build.services

interface QueryFactory {

    DbQueryFactory dbQueryFactory()

    TableQueryFactory tableQueryFactory()

    StatementQueryFactory statementQueryFactory()

}