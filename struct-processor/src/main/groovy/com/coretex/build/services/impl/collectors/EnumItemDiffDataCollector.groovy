package com.coretex.build.services.impl.collectors


import com.coretex.build.data.db.diff.dataholders.EnumItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.EnumValueDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.items.EnumItem
import com.coretex.build.data.items.Item
import com.coretex.build.services.QueryFactory
import org.apache.commons.collections4.CollectionUtils

import java.sql.Connection
import java.util.stream.Collectors

import static com.coretex.common.utils.BuildUtils.execute

class EnumItemDiffDataCollector implements ItemDiffDataCollector<EnumItem> {

    private QueryFactory queryFactory

    EnumValueDiffDataCollector enumValueDiffDataProcessor = new EnumValueDiffDataCollector()

    EnumItemDiffDataCollector(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
        enumValueDiffDataProcessor = new EnumValueDiffDataCollector(queryFactory)
    }

    @Override
    Optional<EnumItemDiffDataHolder> collect(EnumItem item, Connection con) {
        return Optional.of(new EnumItemDiffDataHolder(item: item))
                .map({
                    con.createStatement().withCloseable {statement ->
                        def res = execute(queryFactory.statementQueryFactory().selectEnumType(item), statement)
                        if (CollectionUtils.isNotEmpty(res)) {
                            assert res.size() == 1: "Enum item [${item.code}] has ambigous db records"
                            def map = res.iterator().next()
                            it.item.uuid = map.get("uuid") as UUID
                            it.dbData = map
                        }
                        return it
                    }
                    it.diffDataHolderMap = collectValues(it, con)
                    return it
                })
    }

    Map<UUID, EnumValueDiffDataHolder> collectValues(EnumItemDiffDataHolder holder, Connection connection) {
        Map<UUID, EnumValueDiffDataHolder> attributes = holder.item.values
                .stream()
                .map({
                    return enumValueDiffDataProcessor.collect(it, connection)
                }).flatMap({ it.stream() })
                .collect(Collectors.toMap({ (it.itemUUID) }, { it }))
        attributes.putAll(RemovedEnumValueDiffDataCollector.creteDiffLoader(holder, queryFactory)
                .collect(attributes.values() as List<ItemDiffDataHolder<? extends Item>>, connection)
                .collect(Collectors.toMap({ (it.itemUUID) }, { it })))
        return attributes
    }
}