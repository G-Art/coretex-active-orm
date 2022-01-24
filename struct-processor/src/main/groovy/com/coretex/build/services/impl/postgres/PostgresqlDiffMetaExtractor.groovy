package com.coretex.build.services.impl.postgres


import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.items.*
import com.coretex.build.services.QueryFactory
import com.coretex.build.services.impl.collectors.*

import java.sql.Connection
import java.util.stream.Stream

class PostgresqlDiffMetaExtractor {
    private QueryFactory queryFactory

    private Map<Class<?>, ? extends ItemDiffDataCollector> itemDiffDataProcessorMap

    GenericRemovedItemDiffDataCollector removedItemsDataCollector

    PostgresqlDiffMetaExtractor(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
        removedItemsDataCollector = new GenericRemovedItemDiffDataCollector(queryFactory)
        itemDiffDataProcessorMap = [
                (RegularClassItem.class): new RegularClassItemDiffDataCollector(queryFactory),
                (ClassItem.class)       : new ClassItemDiffDataCollector(queryFactory),
                (EnumItem.class)        : new EnumItemDiffDataCollector(queryFactory),
                (RelationItem.class)    : new RelationItemDiffDataCollector(queryFactory)
        ]
    }

    Optional<ItemDiffDataHolder<? extends Item>> collectMetaData(Item item, Connection con) {
        return Optional.ofNullable(item)
                .flatMap({
                    return process(it, con)
                })
    }

    Stream<ItemDiffDataHolder> collectRemovedItemsMetaData(List<ItemDiffDataHolder> diffDataHolders, Connection con) {
        return removedItemsDataCollector.collect(diffDataHolders, con)
    }

    Optional<ItemDiffDataHolder<? extends Item>> process(Item item, Connection con) {
        if (itemDiffDataProcessorMap.containsKey(item.class)) {
            return itemDiffDataProcessorMap.get(item.class).collect(item, con)
        }
        return Optional.empty()
    }

}
