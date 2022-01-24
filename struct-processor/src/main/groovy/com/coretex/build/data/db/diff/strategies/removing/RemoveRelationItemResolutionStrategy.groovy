package com.coretex.build.data.db.diff.strategies.removing

import com.coretex.build.data.db.builder.EssentialRemoveDataBuilder
import com.coretex.build.data.db.diff.dataholders.RemovedRelationItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.services.QueryFactory

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.ATTRIBUTE_OWNER_RELATION
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.INHERITANCE_RELATION
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ATTRIBUTE_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE

class RemoveRelationItemResolutionStrategy implements DiffResolutionStrategy<RemovedRelationItemDiffDataHolder> {

    private EssentialRemoveDataBuilder essentialDataBuilder = new EssentialRemoveDataBuilder()
    private QueryFactory queryFactory

    RemoveRelationItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    boolean applicable(RemovedRelationItemDiffDataHolder dataHolder) {
        return dataHolder.isRemoved()
    }

    @Override
    Resolution resolve(RemovedRelationItemDiffDataHolder dataHolder) {
        def resolution = new RemoveItemResolution()
        resolution.itemDiffDataHolder = dataHolder

        essentialDataBuilder.itemType(INHERITANCE_RELATION).build(dataHolder).each {
            resolution.commands.addAll(it.commands())
        }
        essentialDataBuilder.itemType(META_TYPE).build(dataHolder).each {
            resolution.commands.addAll(it.commands())
        }

        if (dataHolder.tableOwner()) {
            resolution.commands.add(queryFactory.tableQueryFactory().dropTable(dataHolder.tableName()))
        }

        dataHolder.dataHolderMap.values()
                .each {
                    essentialDataBuilder.itemType(ATTRIBUTE_OWNER_RELATION).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                    essentialDataBuilder.itemType(META_ATTRIBUTE_TYPE).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                }
        return resolution
    }

}
