package com.coretex.build.services.impl.collectors

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.RemovedRelationItemDiffDataHolder
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.services.QueryFactory

import java.sql.Connection
import java.util.stream.Collectors
import java.util.stream.Stream

import static com.coretex.common.utils.BuildUtils.execute

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_RELATION_TYPE

class RemovedRelationItemDiffDataCollector implements RemovedItemDiffDataCollector<RemovedRelationItemDiffDataHolder> {

    QueryFactory queryFactory

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private ClassItem metaRelationTypeItem = items.find { it.code == META_RELATION_TYPE.toString() } as ClassItem

    RemovedRelationItemDiffDataCollector(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    Stream<RemovedRelationItemDiffDataHolder> collect(List<ItemDiffDataHolder<? extends Item>> existHolders, Connection con) {
        con.createStatement().withCloseable {statement ->
            return execute(queryFactory.statementQueryFactory().selectClassItemsByMetaType(metaRelationTypeItem), statement)
                    .stream()
                    .filter({ res -> !existHolders.any { it.itemUUID == res.get("uuid") } })
                    .map({
                        def dataHolder = new RemovedRelationItemDiffDataHolder(dbData: it)
                        dataHolder.dataHolderMap = RemovedAttributeDiffDataCollector.creteDiffLoader(dataHolder, queryFactory)
                                .collect([], con)
                                .collect(Collectors.toMap({ (it.itemUUID) }, { it }))
                        return dataHolder
                    })
        }
    }
}
