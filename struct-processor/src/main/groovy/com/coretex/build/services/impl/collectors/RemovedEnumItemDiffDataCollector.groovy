package com.coretex.build.services.impl.collectors


import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.RemovedEnumItemDiffDataHolder
import com.coretex.build.data.items.Item
import com.coretex.build.services.QueryFactory

import java.sql.Connection
import java.util.stream.Collectors
import java.util.stream.Stream

import static com.coretex.common.utils.BuildUtils.execute

class RemovedEnumItemDiffDataCollector implements RemovedItemDiffDataCollector<RemovedEnumItemDiffDataHolder> {

    QueryFactory queryFactory

    RemovedEnumItemDiffDataCollector(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    Stream<RemovedEnumItemDiffDataHolder> collect(List<ItemDiffDataHolder<? extends Item>> existHolders, Connection con) {
        con.createStatement().withCloseable {statement ->
            return execute(queryFactory.statementQueryFactory().selectAllFromMetaEnumType(), statement)
                    .stream()
                    .filter({ res -> !existHolders.any { it.itemUUID == res.get("uuid") } })
                    .map({
                        def dataHolder = new RemovedEnumItemDiffDataHolder(dbData: it)
                        dataHolder.diffDataHolderMap = RemovedEnumValueDiffDataCollector.creteDiffLoader(dataHolder, queryFactory)
                                .collect([], con)
                                .collect(Collectors.toMap({ (it.itemUUID) }, { it }))
                        return dataHolder
                    })
        }

    }
}
