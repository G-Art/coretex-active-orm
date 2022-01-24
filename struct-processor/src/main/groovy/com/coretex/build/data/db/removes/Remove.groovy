package com.coretex.build.data.db.removes

import com.coretex.build.data.db.DbCommands
import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.services.QueryFactory
import com.coretex.common.DbDialect

abstract class Remove<T> implements DbCommands {

    private DbDialect dialect = CoretexPluginContext.instance.dbDialect

    AbstractItem essentialItem
    T data

    QueryFactory getQueryFactory(){
        return dialect.queryFactory
    }

}
