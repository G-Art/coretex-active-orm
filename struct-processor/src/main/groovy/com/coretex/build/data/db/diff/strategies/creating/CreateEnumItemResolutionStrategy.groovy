package com.coretex.build.data.db.diff.strategies.creating

import com.coretex.build.data.db.builder.EssentialInsertDataBuilder
import com.coretex.build.data.db.diff.dataholders.EnumItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.data.db.diff.strategies.removing.RemoveItemResolution
import com.coretex.build.services.QueryFactory

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.ENUM_VALUE_OWNER_RELATION
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_VALUE_TYPE

class CreateEnumItemResolutionStrategy implements DiffResolutionStrategy<EnumItemDiffDataHolder> {

    private EssentialInsertDataBuilder essentialDataBuilder = new EssentialInsertDataBuilder()

    private QueryFactory queryFactory

    CreateEnumItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    boolean applicable(EnumItemDiffDataHolder dataHolder) {
        return dataHolder.isNew()
    }

    @Override
    Resolution resolve(EnumItemDiffDataHolder dataHolder) {
        def resolution = new RemoveItemResolution()
        resolution.itemDiffDataHolder = dataHolder

        essentialDataBuilder.itemType(META_ENUM_TYPE).build(dataHolder).each {
            resolution.commands.addAll(it.commands())
        }

        dataHolder.diffDataHolderMap.values().each {
            essentialDataBuilder.itemType(META_ENUM_VALUE_TYPE).build(it).each {
                resolution.commands.addAll(it.commands())
            }
            essentialDataBuilder.itemType(ENUM_VALUE_OWNER_RELATION).build(it).each {
                resolution.commands.addAll(it.commands())
            }
        }

        return resolution
    }
}
