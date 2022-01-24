package com.coretex.build.data.db.diff.strategies.updating

import com.coretex.build.data.db.builder.EssentialInsertDataBuilder
import com.coretex.build.data.db.builder.EssentialRemoveDataBuilder
import com.coretex.build.data.db.builder.EssentialUpdateDataBuilder
import com.coretex.build.data.db.diff.dataholders.EnumItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.services.QueryFactory

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*

class UpdateEnumItemResolutionStrategy implements DiffResolutionStrategy<EnumItemDiffDataHolder> {

    private def essentialDataBuilder = new EssentialUpdateDataBuilder()

    private def removeDataBuilder = new EssentialRemoveDataBuilder()
    private def insertDataBuilder = new EssentialInsertDataBuilder()
    private QueryFactory queryFactory

    UpdateEnumItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    boolean applicable(EnumItemDiffDataHolder dataHolder) {
        return !dataHolder.isNew() && !dataHolder.isRemoved()
    }

    @Override
    Resolution resolve(EnumItemDiffDataHolder dataHolder) {
        def resolution = new UpdateItemResolution()
        resolution.itemDiffDataHolder = dataHolder
        essentialDataBuilder.itemType(META_ENUM_TYPE).build(resolution.itemDiffDataHolder).each {
            resolution.commands.addAll(it.commands())
        }

        dataHolder.diffDataHolderMap.values().each {

            if(it.isRemoved()){
                removeDataBuilder.itemType(META_ENUM_VALUE_TYPE).build(it).each {
                    resolution.commands.addAll(it.commands())
                }
                removeDataBuilder.itemType(ENUM_VALUE_OWNER_RELATION).build(it).each {
                    resolution.commands.addAll(it.commands())
                }
            }else{
                if(it.isNew()){
                    insertDataBuilder.itemType(META_ENUM_VALUE_TYPE).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                    insertDataBuilder.itemType(ENUM_VALUE_OWNER_RELATION).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                }else{
                    essentialDataBuilder.itemType(META_ENUM_VALUE_TYPE).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                    essentialDataBuilder.itemType(ENUM_VALUE_OWNER_RELATION).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                }
            }
        }
        return resolution
    }


}
