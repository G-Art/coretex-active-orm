package com.coretex.build.services.impl.collectors

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.ClassItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.services.QueryFactory
import org.apache.commons.collections4.CollectionUtils

import java.sql.Connection
import java.util.stream.Collectors

import static com.coretex.common.utils.BuildUtils.execute

class ClassItemDiffDataCollector implements ItemDiffDataCollector<ClassItem> {

    private QueryFactory queryFactory
    AttributeDiffDataCollector attributeDiffDataProcessor

    ClassItemDiffDataCollector(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
        attributeDiffDataProcessor = new AttributeDiffDataCollector(queryFactory)
    }

    @Override
    Optional<ClassItemDiffDataHolder> collect(ClassItem item, Connection con) {
        return Optional.of(new ClassItemDiffDataHolder(item: item))
                .map({
                    con.createStatement().withCloseable {statement ->
                        def res = execute(queryFactory.statementQueryFactory().selectClassItemByCodeAndType(item), statement)
                        if (CollectionUtils.isNotEmpty(res)) {
                            assert res.size() == 1: "Class item [${item.code}] has ambigous db records"
                            def map = res.iterator().next()
                            it.item.uuid = map.get("uuid") as UUID
                            it.dbData = map
                        }
                        return it
                    }
                    it.dataHolderMap = collectAttributes(it, con)
                    if (it.tableOwner()) {
                        it.hasLocalizedTable = hasLocalizedTable(it, con)
                    }
                    return it
                })

    }

    boolean hasLocalizedTable(ClassItemDiffDataHolder holder, Connection con) {
        return con.createStatement().withCloseable {statement ->
            def res = execute(queryFactory.statementQueryFactory().selectLocTable(holder.tableName()), statement)
            return CollectionUtils.isNotEmpty(res)
        }
    }

    Map<UUID, AttributeItemDiffDataHolder> collectAttributes(ClassItemDiffDataHolder holder, Connection connection) {
        def attr = holder.item.attributes
        if(holder.item.code == 'Generic'){
            def uuidAttr = CoretexPluginContext.instance.uuidAttribute
            uuidAttr.owner = holder.item
            attr.add(uuidAttr)
        }
        Map<UUID, AttributeItemDiffDataHolder> attributes = attr
                .stream()
                .map({
                    return attributeDiffDataProcessor.collect(it, connection)
                })
                .flatMap({ it.stream() })
                .collect(Collectors.toMap({ (it.itemUUID) }, { it }))
        attributes.putAll(RemovedAttributeDiffDataCollector.creteDiffLoader(holder, queryFactory)
                .collect(attributes.values() as List<ItemDiffDataHolder<? extends Item>>, connection)
                .collect(Collectors.toMap({ (it.itemUUID) }, { it })))
        return attributes
    }
}