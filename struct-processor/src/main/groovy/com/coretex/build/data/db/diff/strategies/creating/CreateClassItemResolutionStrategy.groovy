package com.coretex.build.data.db.diff.strategies.creating


import com.coretex.build.data.db.builder.EssentialInsertDataBuilder
import com.coretex.build.data.db.diff.dataholders.ClassItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.services.QueryFactory

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*

class CreateClassItemResolutionStrategy implements DiffResolutionStrategy<ClassItemDiffDataHolder> {

    private EssentialInsertDataBuilder essentialDataBuilder = new EssentialInsertDataBuilder()

    private QueryFactory queryFactory

    CreateClassItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    boolean applicable(ClassItemDiffDataHolder dataHolder) {
        return dataHolder.isNew()
    }

    @Override
    Resolution resolve(ClassItemDiffDataHolder dataHolder) {
        def resolution = new CreateItemResolution()
        resolution.itemDiffDataHolder = dataHolder

        if (dataHolder.tableOwner()) {
            resolution.commands.addAll(creteTableCommands(dataHolder))
        }
        essentialDataBuilder.itemType(META_TYPE).build(dataHolder)
                .each {
                    resolution.commands.addAll(it.commands())
                }
        essentialDataBuilder.itemType(INHERITANCE_RELATION).build(dataHolder)
                .each {
                    resolution.commands.addAll(it.commands())
                }

        dataHolder.dataHolderMap.values().each {
            essentialDataBuilder.itemType(META_ATTRIBUTE_TYPE).build(it)
                    .each {
                        resolution.commands.addAll(it.commands())
                    }
            essentialDataBuilder.itemType(ATTRIBUTE_OWNER_RELATION).build(it)
                    .each {
                        resolution.commands.addAll(it.commands())
                    }
        }
        return resolution
    }

    Collection<? extends String> creteTableCommands(ClassItemDiffDataHolder classItemDiffDataHolder) {
        def commands = []
        def table = classItemDiffDataHolder.convertToTable()
        commands.add(queryFactory.tableQueryFactory().createTable(table))
        if (table.localeSupportTableRequired) {
            commands.add(queryFactory.tableQueryFactory().createLocTable(table))
        }
        if (!table.indexes.isEmpty()) {
            table.indexes.forEach({ k, v ->
                commands.add( queryFactory.tableQueryFactory().crateIndex(k, table.name, v.tableFields))
            })
        }

        return commands
    }
}
