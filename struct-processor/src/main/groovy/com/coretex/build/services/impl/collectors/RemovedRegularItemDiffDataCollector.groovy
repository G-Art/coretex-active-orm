package com.coretex.build.services.impl.collectors

import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.RemovedRegularClassItemDiffDataHolder
import com.coretex.build.data.items.Item
import com.coretex.build.services.QueryFactory

import java.sql.Connection
import java.util.stream.Stream

import static com.coretex.common.utils.BuildUtils.execute

class RemovedRegularItemDiffDataCollector implements RemovedItemDiffDataCollector<RemovedRegularClassItemDiffDataHolder> {

    QueryFactory queryFactory

    RemovedRegularItemDiffDataCollector(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    Stream<RemovedRegularClassItemDiffDataHolder> collect(List<ItemDiffDataHolder<? extends Item>> existHolders, Connection con) {
        con.createStatement().withCloseable {statement ->
            return execute(queryFactory.statementQueryFactory().selectAllFromRegularType(), statement)
                    .stream()
                    .filter({ res -> !existHolders.any { it.itemUUID == res.get("uuid")} })
                    .map({
                        return new RemovedRegularClassItemDiffDataHolder(dbData: it)
                    })
        }
    }
}
