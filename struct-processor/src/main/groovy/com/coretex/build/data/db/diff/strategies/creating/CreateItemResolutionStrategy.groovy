package com.coretex.build.data.db.diff.strategies.creating


import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.data.items.*
import com.coretex.build.services.QueryFactory

class CreateItemResolutionStrategy implements DiffResolutionStrategy<ItemDiffDataHolder<? extends Item>> {

    private QueryFactory queryFactory

    LinkedHashMap<Class<? extends Item>, DiffResolutionStrategy<? extends ItemDiffDataHolder<? extends Item>>> insertItemStrategies

    CreateItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
        insertItemStrategies = [
                (ClassItem.class)       : new CreateClassItemResolutionStrategy(queryFactory),
                (EnumItem.class)        : new CreateEnumItemResolutionStrategy(queryFactory),
                (RelationItem.class)    : new CreateRelationItemResolutionStrategy(queryFactory),
                (RegularClassItem.class): new CreateRegularItemResolutionStrategy(queryFactory)
        ]
    }

    @Override
    boolean applicable(ItemDiffDataHolder<? extends Item> dataHolder) {
        return dataHolder.isNew()
    }

    @Override
    Resolution resolve(ItemDiffDataHolder<? extends Item> dataHolder) {
        def strategy = insertItemStrategies.get(dataHolder.itemClass)
        if (Objects.nonNull(strategy) && strategy.applicable(dataHolder)) {
            return strategy.resolve(dataHolder)
        }
        return null
    }

}
