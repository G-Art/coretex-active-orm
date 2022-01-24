package com.coretex.build.services.impl.collectors

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.RemovedClassItemDiffDataHolder
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.services.QueryFactory

import java.sql.Connection
import java.util.stream.Collectors
import java.util.stream.Stream

import static com.coretex.common.utils.BuildUtils.execute

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE

class RemovedClassItemDiffDataCollector implements RemovedItemDiffDataCollector<RemovedClassItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private ClassItem metaTypeItem = items.find { it.code == META_TYPE.toString() } as ClassItem

    QueryFactory queryFactory

    RemovedClassItemDiffDataCollector(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    Stream<RemovedClassItemDiffDataHolder> collect(List<ItemDiffDataHolder<? extends Item>> existHolders, Connection con) {
        con.createStatement().withCloseable {statement ->
            return execute(queryFactory.statementQueryFactory().selectClassItemsByMetaType(metaTypeItem), statement)
                    .stream()
                    .filter({ res -> !existHolders.any { it.itemUUID == res.get("uuid") } })
                    .map({
                        def dataHolder = new RemovedClassItemDiffDataHolder(dbData: it)
                        dataHolder.dataHolderMap = RemovedAttributeDiffDataCollector.creteDiffLoader(dataHolder, queryFactory)
                                .collect([], con)
                                .collect(Collectors.toMap({ (it.itemUUID) }, { it }))
                        return dataHolder
                    })
        }
    }
}
