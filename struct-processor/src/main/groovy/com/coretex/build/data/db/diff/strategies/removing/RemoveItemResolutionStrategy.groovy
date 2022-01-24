package com.coretex.build.data.db.diff.strategies.removing

import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.data.items.*
import com.coretex.build.services.QueryFactory

class RemoveItemResolutionStrategy implements DiffResolutionStrategy<ItemDiffDataHolder<? extends Item>> {

    private QueryFactory queryFactory

    LinkedHashMap<Class<? extends Item>, DiffResolutionStrategy<? extends ItemDiffDataHolder<? extends Item>>> removeItemStrategies

    RemoveItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
        removeItemStrategies = [
                (ClassItem.class)       : new RemoveClassItemResolutionStrategy(queryFactory),
                (EnumItem.class)        : new RemoveEnumItemResolutionStrategy(queryFactory),
                (RelationItem.class)    : new RemoveRelationItemResolutionStrategy(queryFactory),
                (RegularClassItem.class): new RemoveRegularItemResolutionStrategy(queryFactory)
        ]
    }

    @Override
    boolean applicable(ItemDiffDataHolder<? extends Item> dataHolder) {
        return dataHolder.isRemoved()
    }

    @Override
    Resolution resolve(ItemDiffDataHolder<? extends Item> dataHolder) {

        def strategy = removeItemStrategies.get(dataHolder.itemClass)
        if (Objects.nonNull(strategy) && strategy.applicable(dataHolder)) {
            return strategy.resolve(dataHolder)
        }
        return null
    }
}
