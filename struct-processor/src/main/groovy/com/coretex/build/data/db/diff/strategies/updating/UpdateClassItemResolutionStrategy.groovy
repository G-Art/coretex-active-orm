package com.coretex.build.data.db.diff.strategies.updating

import com.coretex.Constants
import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.converters.impl.AttributeToTableFieldConverter
import com.coretex.build.data.db.builder.EssentialInsertDataBuilder
import com.coretex.build.data.db.builder.EssentialRemoveDataBuilder
import com.coretex.build.data.db.builder.EssentialUpdateDataBuilder
import com.coretex.build.data.db.diff.dataholders.ClassItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.data.db.diff.strategies.creating.CreateClassItemResolutionStrategy
import com.coretex.build.data.db.diff.strategies.removing.RemoveClassItemResolutionStrategy
import com.coretex.build.services.QueryFactory
import com.coretex.common.utils.BuildUtils
import com.google.common.collect.Lists
import org.apache.commons.collections.CollectionUtils

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*

class UpdateClassItemResolutionStrategy implements DiffResolutionStrategy<ClassItemDiffDataHolder> {

    private CoretexPluginContext buildContext = CoretexPluginContext.instance
    AttributeToTableFieldConverter fieldConverter = new AttributeToTableFieldConverter(buildContext.dbDialect)
    private QueryFactory queryFactory

    private def essentialDataBuilder = new EssentialUpdateDataBuilder()

    private def removeDataBuilder = new EssentialRemoveDataBuilder()
    private def insertDataBuilder = new EssentialInsertDataBuilder()

    UpdateClassItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    boolean applicable(ClassItemDiffDataHolder dataHolder) {
        return !dataHolder.isNew() && !dataHolder.isRemoved()
    }

    @Override
    Resolution resolve(ClassItemDiffDataHolder dataHolder) {
        def resolution = new UpdateItemResolution()
        resolution.itemDiffDataHolder = dataHolder

        updateTable(resolution)
        updateMetaData(resolution)

        return resolution
    }

    void updateTable(UpdateItemResolution resolution) {

        def diffDataHolder = resolution.itemDiffDataHolder as ClassItemDiffDataHolder

        updateColumns(resolution, diffDataHolder)
        updateIndexes(resolution, diffDataHolder)

        if (diffDataHolder.tableOwner()) {
            def table = diffDataHolder.convertToTable()
            if (diffDataHolder.hasLocalizedTable) {
                if (!table.localeSupportTableRequired) {
                    resolution.commands.add(queryFactory.tableQueryFactory().dropTable(diffDataHolder.tableName()))
                }
            } else {
                if (table.localeSupportTableRequired) {
                    resolution.commands.add(queryFactory.tableQueryFactory().createLocTable(table))
                }
            }
        }
    }

    void updateMetaData(UpdateItemResolution resolution) {
        essentialDataBuilder.itemType(META_TYPE).build(resolution.itemDiffDataHolder).each {
            resolution.commands.addAll(it.commands())
        }
        essentialDataBuilder.itemType(INHERITANCE_RELATION).build(resolution.itemDiffDataHolder).each {
            resolution.commands.addAll(it.commands())
        }
        def holder = resolution.itemDiffDataHolder as ClassItemDiffDataHolder

        holder.dataHolderMap.values().each {

            if (it.isRemoved()) {
                removeDataBuilder.itemType(META_ATTRIBUTE_TYPE).build(it).each {
                    resolution.commands.addAll(it.commands())
                }
                removeDataBuilder.itemType(ATTRIBUTE_OWNER_RELATION).build(it).each {
                    resolution.commands.addAll(it.commands())
                }
            } else {
                if (it.isNew()) {
                    insertDataBuilder.itemType(META_ATTRIBUTE_TYPE).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                    insertDataBuilder.itemType(ATTRIBUTE_OWNER_RELATION).build(it).each {
                        resolution.commands.addAll(it.commands())
                    }
                } else {
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

    Map<String, List<String>> collectIndexInfo(ClassItemDiffDataHolder dataHolder) {
        Map<String, List<String>> indexes = [:]
        for (def col : BuildUtils.collectAllAttributesForItem(dataHolder.item)) {
            col.index.each { iname ->
                indexes.computeIfPresent(Constants.INDEX_PREFIX + iname, { k, v ->
                    v.add( col.columnName.toLowerCase())
                    return v
                })
                indexes.computeIfAbsent(Constants.INDEX_PREFIX + iname, { Lists.newArrayList(col.columnName.toLowerCase()) })
            }
        }
        return indexes
    }

    void updateColumns(UpdateItemResolution resolution, ClassItemDiffDataHolder diffDataHolder) {
        diffDataHolder.dataHolderMap.values()
                .findAll { it.columnName() != "uuid" }
                .each {
                    if (it.isNew() && !it.item.isRelation()) {
                        def tableField = fieldConverter.doConverting(it.item)
                        resolution.commands.add(queryFactory.tableQueryFactory().alterTableAdd(diffDataHolder.tableName(), tableField.name, tableField.sqlType))
                    }
                    if (it.isRemoved() && it.columnName() != null) {
                        resolution.commands.add(queryFactory.tableQueryFactory().alterTableDrop(diffDataHolder.tableName(), it.columnName()))
                    }
                }
    }

    void updateIndexes(UpdateItemResolution resolution, ClassItemDiffDataHolder dataHolder) {
        if (dataHolder.tableOwner()) {
            def indexes = dataHolder.convertToTable().indexes
            def indexInfo = collectIndexInfo(dataHolder)

            indexInfo.entrySet()
                    .findAll({
                        !indexes.containsKey(it.key) ||
                                !CollectionUtils.isEqualCollection(indexes.get(it.key)
                                        .tableFields
                                        .collect({ it.name.toLowerCase() }), it.value)
                    })
                    .each({
                        resolution.commands.add(queryFactory.tableQueryFactory().dropIndex(it.key))
                    })
            indexes.entrySet()
                    .findAll({ e ->
                        !indexInfo.containsKey(e.key) ||
                                !CollectionUtils.isEqualCollection(indexInfo.get(e.key), e.value.tableFields.collect({ it.name.toLowerCase() }))
                    })
                    .each({
                        resolution.commands.add(queryFactory.tableQueryFactory().crateIndex(it.key, dataHolder.tableName(), it.value.tableFields))
                    })
        }
    }
}
