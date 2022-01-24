package com.coretex.build.data.db.diff.strategies.creating


import com.coretex.build.data.db.builder.EssentialInsertDataBuilder
import com.coretex.build.data.db.diff.dataholders.RelationItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.services.QueryFactory

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*

class CreateRelationItemResolutionStrategy implements DiffResolutionStrategy<RelationItemDiffDataHolder> {

    private EssentialInsertDataBuilder essentialDataBuilder = new EssentialInsertDataBuilder()

    private QueryFactory queryFactory

    CreateRelationItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    boolean applicable(RelationItemDiffDataHolder dataHolder) {
        return dataHolder.isNew()
    }

    @Override
    Resolution resolve(RelationItemDiffDataHolder dataHolder) {
        def resolution = new CreateItemResolution()
        resolution.itemDiffDataHolder = dataHolder

        if (dataHolder.tableOwner()) {
            resolution.commands.addAll(creteTableCommands(dataHolder))
        }

        return resolution
    }

    Collection<? extends String> creteTableCommands(RelationItemDiffDataHolder classItemDiffDataHolder) {
        def commands = []
        def table = classItemDiffDataHolder.convertToTable()
        commands.add(queryFactory.tableQueryFactory().createTable(table))

        essentialDataBuilder.itemType(META_TYPE).build(classItemDiffDataHolder)
                .each {
                    commands.addAll(it.commands())
                }
        essentialDataBuilder.itemType(INHERITANCE_RELATION).build(classItemDiffDataHolder)
                .each {
                    commands.addAll(it.commands())
                }

        classItemDiffDataHolder.dataHolderMap.values().each {
            essentialDataBuilder.itemType(META_ATTRIBUTE_TYPE).build(it)
                    .each {
                        commands.addAll(it.commands())
                    }
            essentialDataBuilder.itemType(ATTRIBUTE_OWNER_RELATION).build(it)
                    .each {
                        commands.addAll(it.commands())
                    }
        }
        return commands
    }
}
