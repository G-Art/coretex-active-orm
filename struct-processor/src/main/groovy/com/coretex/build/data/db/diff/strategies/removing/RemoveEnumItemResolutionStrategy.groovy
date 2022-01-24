package com.coretex.build.data.db.diff.strategies.removing

import com.coretex.build.data.db.builder.EssentialRemoveDataBuilder
import com.coretex.build.data.db.diff.dataholders.RemovedEnumItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.services.QueryFactory

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*

class RemoveEnumItemResolutionStrategy implements DiffResolutionStrategy<RemovedEnumItemDiffDataHolder> {

    private EssentialRemoveDataBuilder essentialDataBuilder = new EssentialRemoveDataBuilder()

    private QueryFactory queryFactory

    RemoveEnumItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    boolean applicable(RemovedEnumItemDiffDataHolder dataHolder) {
        return dataHolder.isRemoved()
    }

    @Override
    Resolution resolve(RemovedEnumItemDiffDataHolder dataHolder) {
        def resolution = new RemoveItemResolution()
        resolution.itemDiffDataHolder = dataHolder


        dataHolder.diffDataHolderMap.values().each {
            essentialDataBuilder.itemType(ENUM_VALUE_OWNER_RELATION).build(it).each {
                resolution.commands.addAll(it.commands())
            }
            essentialDataBuilder.itemType(META_ENUM_VALUE_TYPE).build(it).each {
                resolution.commands.addAll(it.commands())
            }
        }
        essentialDataBuilder.itemType(META_ENUM_TYPE).build(dataHolder).each {
            resolution.commands.addAll(it.commands())
        }

        return resolution
    }

}
