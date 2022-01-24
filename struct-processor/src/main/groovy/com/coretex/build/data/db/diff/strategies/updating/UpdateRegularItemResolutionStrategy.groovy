package com.coretex.build.data.db.diff.strategies.updating


import com.coretex.build.data.db.builder.EssentialUpdateDataBuilder
import com.coretex.build.data.db.diff.dataholders.RegularClassItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.services.QueryFactory

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE

class UpdateRegularItemResolutionStrategy implements DiffResolutionStrategy<RegularClassItemDiffDataHolder> {

    private EssentialUpdateDataBuilder essentialDataBuilder = new EssentialUpdateDataBuilder()
    private QueryFactory queryFactory

    UpdateRegularItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    boolean applicable(RegularClassItemDiffDataHolder dataHolder) {
        return !dataHolder.isNew() && !dataHolder.isRemoved()
    }

    @Override
    Resolution resolve(RegularClassItemDiffDataHolder dataHolder) {
        def resolution = new UpdateItemResolution()
        resolution.itemDiffDataHolder = dataHolder

        essentialDataBuilder.itemType(REGULAR_TYPE).build(dataHolder).each {
            resolution.commands.addAll(it.commands())
        }
        return resolution
    }
}
