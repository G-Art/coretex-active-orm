package com.coretex.build.services.impl.postgres.query

import com.coretex.build.services.DbQueryFactory
import com.coretex.build.services.QueryFactory
import com.coretex.build.services.StatementQueryFactory
import com.coretex.build.services.TableQueryFactory

class PostgresqlQueryFactory implements QueryFactory {

    def tableQueryFactory = new PostgresqlTableQueryFactory()
    def dbQueryFactory = new PostgresqlDbQueryFactory()
    def statementQueryFactory = new PostgresqlStatementQueryFactory()

    @Override
    DbQueryFactory dbQueryFactory() {
        return dbQueryFactory
    }

    @Override
    TableQueryFactory tableQueryFactory() {
        return tableQueryFactory
    }

    @Override
    StatementQueryFactory statementQueryFactory() {
        return statementQueryFactory
    }
}
