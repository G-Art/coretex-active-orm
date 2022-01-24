package com.coretex.common

import com.coretex.build.services.DBDiffService
import com.coretex.build.services.DbService
import com.coretex.build.services.QueryFactory
import com.coretex.build.services.TypeAppropriateService
import com.coretex.build.services.impl.mysql.MySqlDBDiffService
import com.coretex.build.services.impl.mysql.query.MySqlQueryFactory
import com.coretex.build.services.impl.mysql.MySqlTypeAppropriateService
import com.coretex.build.services.impl.mysql.MySqlDbService
import com.coretex.build.services.impl.postgres.PostgresqlDBDiffService
import com.coretex.build.services.impl.postgres.PostgresqlDbService
import com.coretex.build.services.impl.postgres.query.PostgresqlQueryFactory
import com.coretex.build.services.impl.postgres.PostgresqlTypeAppropriateService
import org.gradle.internal.impldep.org.apache.http.MethodNotSupportedException

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
enum DbDialect {

    POSTGRESQL('postgresql', new PostgresqlQueryFactory()) {
        @Override
        TypeAppropriateService getTypeAppropriateService() {
            return new PostgresqlTypeAppropriateService()
        }

        @Override
        DbService getDbService() {
            return new PostgresqlDbService(getQueryFactory())
        }

        @Override
        DBDiffService dbDiffService() {
            return new PostgresqlDBDiffService(getQueryFactory())
        }
    },

    MYSQL('mysql', new MySqlQueryFactory()) {
        @Override
        TypeAppropriateService getTypeAppropriateService() {
            return new MySqlTypeAppropriateService();
        }

        @Override
        DbService getDbService() {
            return new MySqlDbService(getQueryFactory())
        }

        @Override
        DBDiffService dbDiffService() {
            return new MySqlDBDiffService(getQueryFactory())
        }
    },

    ORACLE('oracle', null) {
        @Override
        TypeAppropriateService getTypeAppropriateService() {
            throw new MethodNotSupportedException('Oracle dialect not supported yet')
        }

        @Override
        DbService getDbService() {
            throw new MethodNotSupportedException('Oracle dialect not supported yet')
        }
        @Override
        DBDiffService dbDiffService() {
            throw new MethodNotSupportedException('Oracle dialect not supported yet')
        }
    }

    private String dialect
    private QueryFactory queryFactory

    DbDialect(String dialect, QueryFactory queryFactory ) {
        this.dialect = dialect
        this.queryFactory = queryFactory
    }

    abstract TypeAppropriateService getTypeAppropriateService()
    abstract DbService getDbService()
    abstract DBDiffService dbDiffService()

    QueryFactory getQueryFactory() {
        return queryFactory
    }

    @Override
    String toString() {
        return dialect
    }

    static DbDialect fromString(String dialect) {
        if (dialect == null) {
            return null
        }

        for (DbDialect state : values()) {
            if (state.toString() == dialect) {
                return state
            }
        }

        return null
    }
}