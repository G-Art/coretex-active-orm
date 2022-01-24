package com.coretex.build.services.impl.collectors

import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.items.Item
import com.coretex.build.services.QueryFactory

import java.sql.Connection
import java.util.function.Function
import java.util.stream.Stream

class GenericRemovedItemDiffDataCollector implements RemovedItemDiffDataCollector<ItemDiffDataHolder> {

    QueryFactory queryFactory

    RemovedEnumItemDiffDataCollector removedEnumItemDiffCollector
    RemovedRegularItemDiffDataCollector removedRegularItemDiffCollector
    RemovedClassItemDiffDataCollector removedClassItemDiffCollector
    RemovedRelationItemDiffDataCollector removedRelationItemDiffCollector

    GenericRemovedItemDiffDataCollector(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
        removedEnumItemDiffCollector = new RemovedEnumItemDiffDataCollector(queryFactory)
        removedRegularItemDiffCollector = new RemovedRegularItemDiffDataCollector(queryFactory)
        removedClassItemDiffCollector = new RemovedClassItemDiffDataCollector(queryFactory)
        removedRelationItemDiffCollector = new RemovedRelationItemDiffDataCollector(queryFactory)
    }

    Stream<ItemDiffDataHolder> collect(List<ItemDiffDataHolder<? extends Item>> existHolders, Connection connection) {
        return Stream.of(
                removedClassItemDiffCollector.collect(existHolders, connection),
                removedRelationItemDiffCollector.collect(existHolders, connection),
                removedRegularItemDiffCollector.collect(existHolders, connection),
                removedEnumItemDiffCollector.collect(existHolders, connection)
        ).flatMap(Function.identity())

    }
}
