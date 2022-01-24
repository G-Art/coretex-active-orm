package com.coretex.build.services.impl.collectors

import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.build.services.QueryFactory
import org.apache.commons.collections4.CollectionUtils

import java.sql.Connection
import java.util.stream.Collectors

import static com.coretex.common.utils.BuildUtils.execute

class AttributeDiffDataCollector implements ItemDiffDataCollector<Attribute<Item>> {
    private QueryFactory queryFactory

    AttributeDiffDataCollector(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    Optional<AttributeItemDiffDataHolder> collect(Attribute<Item> item, Connection con) {
        return Optional.of(new AttributeItemDiffDataHolder(item: item))
                .map({
                    con.createStatement().withCloseable {statement ->
                        def res = execute(queryFactory.statementQueryFactory()
                                .selectAttributesByOwnerUUIDAndAttributeNameAndColumnName(item.owner.uuid.toString(), item.name, item.columnName), statement)
                        if (CollectionUtils.isNotEmpty(res)) {
                            assert res.size() == 1: "Attribute [${item.owner.code}::${item.code}] has ambigous db records"
                            def map = res.iterator().next()
                            it.item.uuid = map.get("uuid") as UUID
                            it.dbData = map
                        }
                        return it
                    }
                    it.indexes = loadIndex(con, it.item.owner.tableName, it.columnName())
                    return it
                })
    }

    List<String> loadIndex(Connection con, String tableName, String columnName) {
        con.createStatement().withCloseable {statement ->
            return execute(queryFactory.statementQueryFactory().selectIndexesByTableAndColumn(tableName, columnName), statement)
                    .stream()
                    .map({ it.get("indexName") })
                    .filter({ (it != "${tableName.toLowerCase()}_pkey") })
                    .collect(Collectors.toList())
        }
    }
}