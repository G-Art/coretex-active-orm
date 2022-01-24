package com.coretex.build.services.impl.collectors

import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.RemovedAttributeItemDiffDataHolder
import com.coretex.build.data.items.Item
import com.coretex.build.services.QueryFactory

import java.sql.Connection
import java.util.stream.Collectors
import java.util.stream.Stream

import static com.coretex.common.utils.BuildUtils.execute

class RemovedAttributeDiffDataCollector implements RemovedItemDiffDataCollector<RemovedAttributeItemDiffDataHolder> {

    private QueryFactory queryFactory

    private RemovedAttributeDiffDataCollector(AbstractClassItemDiffDataHolder item, QueryFactory queryFactory) {
        this.item = item
        this.queryFactory = queryFactory
    }

    AbstractClassItemDiffDataHolder item

    @Override
    Stream<RemovedAttributeItemDiffDataHolder> collect(List<ItemDiffDataHolder<? extends Item>> existHolders, Connection con) {

        con.createStatement().withCloseable {statement ->
            return execute(queryFactory.statementQueryFactory().selectAttributesByOwnerUUID(item.itemUUID.toString()), statement)
                    .stream()
                    .filter({ res -> !existHolders.any { it.itemUUID == res.get("uuid") || res.get('c_attributename') == 'uuid' } })
            .map({
                def dataHolder = new RemovedAttributeItemDiffDataHolder(dbData: it)
                dataHolder.indexes = loadIndex(con, dataHolder.columnName())
                return dataHolder
            })
        }

    }

    static RemovedAttributeDiffDataCollector creteDiffLoader(AbstractClassItemDiffDataHolder item, QueryFactory queryFactory) {
        return new RemovedAttributeDiffDataCollector(item, queryFactory)
    }

    List<String> loadIndex(Connection con, String columnName) {
        con.createStatement().withCloseable {statement ->
            return execute(queryFactory.statementQueryFactory().selectIndexesByTableAndColumn(item.tableName(), columnName), statement)
                    .stream()
                    .map({ it.get("indexName") })
                    .filter({ (it != "${item.tableName().toLowerCase()}_pkey") })
                    .collect(Collectors.toList())
        }
    }

}