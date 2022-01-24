package com.coretex.build.data.db.diff.strategies.removing

import com.coretex.build.data.db.builder.EssentialRemoveDataBuilder
import com.coretex.build.data.db.diff.dataholders.RemovedRegularClassItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.services.QueryFactory

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE

class RemoveRegularItemResolutionStrategy implements DiffResolutionStrategy<RemovedRegularClassItemDiffDataHolder> {

    private EssentialRemoveDataBuilder essentialDataBuilder = new EssentialRemoveDataBuilder()
    private QueryFactory queryFactory

    RemoveRegularItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    boolean applicable(RemovedRegularClassItemDiffDataHolder dataHolder) {
        return dataHolder.isRemoved()
    }

    @Override
    Resolution resolve(RemovedRegularClassItemDiffDataHolder dataHolder) {
        def resolution = new RemoveItemResolution()
        resolution.itemDiffDataHolder = dataHolder

        essentialDataBuilder.itemType(REGULAR_TYPE).build(dataHolder).each {
            resolution.commands.addAll(it.commands())
        }
        return resolution
    }
}
