package com.coretex.build.data.db.diff.strategies.updating

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.converters.impl.AttributeToTableFieldConverter
import com.coretex.build.data.db.builder.EssentialInsertDataBuilder
import com.coretex.build.data.db.builder.EssentialRemoveDataBuilder
import com.coretex.build.data.db.builder.EssentialUpdateDataBuilder
import com.coretex.build.data.db.diff.dataholders.ClassItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.RelationItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.data.db.diff.strategies.creating.CreateClassItemResolutionStrategy
import com.coretex.build.data.db.diff.strategies.removing.RemoveClassItemResolutionStrategy
import com.coretex.build.services.QueryFactory
import com.coretex.common.utils.BuildUtils
import com.google.common.collect.Lists
import org.apache.commons.collections.CollectionUtils

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*

class UpdateRelationItemResolutionStrategy implements DiffResolutionStrategy<RelationItemDiffDataHolder> {

    private CoretexPluginContext buildContext = CoretexPluginContext.instance
    AttributeToTableFieldConverter fieldConverter = new AttributeToTableFieldConverter(buildContext.dbDialect)
    private QueryFactory queryFactory

    private def essentialDataBuilder = new EssentialUpdateDataBuilder()

    private def removeDataBuilder = new EssentialRemoveDataBuilder()
    private def insertDataBuilder = new EssentialInsertDataBuilder()

    UpdateRelationItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    boolean applicable(RelationItemDiffDataHolder dataHolder) {
        return !dataHolder.isNew() && !dataHolder.isRemoved()
    }

    @Override
    Resolution resolve(RelationItemDiffDataHolder dataHolder) {
        def resolution = new UpdateItemResolution()
        resolution.itemDiffDataHolder = dataHolder

        updateTable(resolution)
        updateMetaData(resolution)

        return resolution
    }

    void updateTable(UpdateItemResolution resolution) {

        def diffDataHolder = resolution.itemDiffDataHolder as RelationItemDiffDataHolder

        updateColumns(resolution, diffDataHolder)
    }

    void updateMetaData(UpdateItemResolution resolution) {
        essentialDataBuilder.itemType(META_TYPE).build(resolution.itemDiffDataHolder).each {
            resolution.commands.addAll(it.commands())
        }
        essentialDataBuilder.itemType(INHERITANCE_RELATION).build(resolution.itemDiffDataHolder).each {
            resolution.commands.addAll(it.commands())
        }
        def holder = resolution.itemDiffDataHolder as RelationItemDiffDataHolder

        holder.dataHolderMap.values().each {

            if(it.isRemoved()){
                removeDataBuilder.itemType(META_ATTRIBUTE_TYPE).build(it).each {
                    resolution.commands.addAll(it.commands())
                }
                removeDataBuilder.itemType(ATTRIBUTE_OWNER_RELATION).build(it).each {
                    resolution.commands.addAll(it.commands())
                }
            }else{
                if(it.isNew()){
                    insertDataBuilder.itemType(META_ATTRIBUTE_TYPE).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                    insertDataBuilder.itemType(ATTRIBUTE_OWNER_RELATION).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                }else{
                    essentialDataBuilder.itemType(META_ATTRIBUTE_TYPE).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                    essentialDataBuilder.itemType(ATTRIBUTE_OWNER_RELATION).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                }
            }

        }

    }

    void updateColumns(UpdateItemResolution resolution, RelationItemDiffDataHolder diffDataHolder) {
        diffDataHolder.dataHolderMap.values()
                .each {
                    if (it.isNew()) {
                        def tableField = fieldConverter.doConverting(it.item)
                        resolution.commands.add(queryFactory.tableQueryFactory().alterTableAdd(diffDataHolder.tableName(), tableField.name, tableField.sqlType))
                    }
                    if (it.isRemoved()) {
                        resolution.commands.add(queryFactory.tableQueryFactory().alterTableDrop(diffDataHolder.tableName(), it.item.columnName))
                    }
                }
    }

}
