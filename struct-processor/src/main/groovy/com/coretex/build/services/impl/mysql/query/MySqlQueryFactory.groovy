package com.coretex.build.services.impl.mysql.query

import com.coretex.build.services.DbQueryFactory
import com.coretex.build.services.QueryFactory
import com.coretex.build.services.StatementQueryFactory
import com.coretex.build.services.TableQueryFactory

class MySqlQueryFactory implements QueryFactory {

    def tableQueryFactory = new MySqlTableQueryFactory()
    def dbQueryFactory = new MySqlDbQueryFactory()
    def statementQueryFactory = new MySqlStatementQueryFactory()

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
