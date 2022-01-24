package com.coretex.build.services.impl.collectors

import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.RemovedEnumValueDiffDataHolder
import com.coretex.build.data.items.Item
import com.coretex.build.services.QueryFactory

import java.sql.Connection
import java.util.stream.Stream

import static com.coretex.common.utils.BuildUtils.execute

class RemovedEnumValueDiffDataCollector implements RemovedItemDiffDataCollector<RemovedEnumValueDiffDataHolder> {

    QueryFactory queryFactory

    ItemDiffDataHolder<? extends Item> item

    private RemovedEnumValueDiffDataCollector(ItemDiffDataHolder<? extends Item> item, QueryFactory queryFactory) {
        this.item = item
        this.queryFactory = queryFactory
    }

    @Override
    Stream<RemovedEnumValueDiffDataHolder> collect(List<ItemDiffDataHolder<? extends Item>> existHolders, Connection con) {
        con.createStatement().withCloseable {statement ->
            return execute(queryFactory.statementQueryFactory().selectEnumValueTypeByUUID(item.itemUUID.toString()), statement)
                    .stream()
                    .filter({ res -> !existHolders.any { it.itemUUID == res.get("uuid")} })
                    .map({
                        return new RemovedEnumValueDiffDataHolder(dbData: it)
                    })
        }
    }

    static RemovedEnumValueDiffDataCollector creteDiffLoader(ItemDiffDataHolder<? extends Item> item, QueryFactory queryFactory){
        return new RemovedEnumValueDiffDataCollector(item, queryFactory)
    }

}
