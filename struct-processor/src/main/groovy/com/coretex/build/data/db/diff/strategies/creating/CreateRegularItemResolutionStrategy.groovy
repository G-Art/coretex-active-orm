package com.coretex.build.data.db.diff.strategies.creating

import com.coretex.build.data.db.builder.EssentialInsertDataBuilder
import com.coretex.build.data.db.diff.dataholders.RegularClassItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.data.db.diff.strategies.removing.RemoveItemResolution
import com.coretex.build.services.QueryFactory

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE

class CreateRegularItemResolutionStrategy implements DiffResolutionStrategy<RegularClassItemDiffDataHolder> {

    private EssentialInsertDataBuilder essentialDataBuilder = new EssentialInsertDataBuilder()
    private QueryFactory queryFactory

    CreateRegularItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }
    @Override
    boolean applicable(RegularClassItemDiffDataHolder dataHolder) {
        return dataHolder.isNew()
    }

    @Override
    Resolution resolve(RegularClassItemDiffDataHolder dataHolder) {
        def resolution = new RemoveItemResolution()
        resolution.itemDiffDataHolder = dataHolder

        essentialDataBuilder.itemType(REGULAR_TYPE).build(dataHolder).each {
            resolution.commands.addAll(it.commands())
        }
        return resolution
    }
}
