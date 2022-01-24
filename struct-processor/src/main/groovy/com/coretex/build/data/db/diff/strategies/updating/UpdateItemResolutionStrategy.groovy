package com.coretex.build.data.db.diff.strategies.updating

import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.data.items.*
import com.coretex.build.services.QueryFactory

class UpdateItemResolutionStrategy implements DiffResolutionStrategy<ItemDiffDataHolder<? extends Item>>  {

    private QueryFactory queryFactory

    LinkedHashMap<Class<? extends Item>, DiffResolutionStrategy<? extends ItemDiffDataHolder<? extends Item>>> itemStrategies

    UpdateItemResolutionStrategy(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
        itemStrategies = [
                (ClassItem.class)       : new UpdateClassItemResolutionStrategy(queryFactory),
                (RelationItem.class)    : new UpdateRelationItemResolutionStrategy(queryFactory),
                (EnumItem.class)        : new UpdateEnumItemResolutionStrategy(queryFactory),
                (RegularClassItem.class): new UpdateRegularItemResolutionStrategy(queryFactory)
        ]
    }

    @Override
    boolean applicable(ItemDiffDataHolder<? extends Item> dataHolder) {
        return !dataHolder.isNew() && !dataHolder.isRemoved()
    }

    @Override
    Resolution resolve(ItemDiffDataHolder<? extends Item> dataHolder) {
        def strategy = itemStrategies.get(dataHolder.itemClass)
        if (Objects.nonNull(strategy) && strategy.applicable(dataHolder)) {
            return strategy.resolve(dataHolder)
        }
        return null
    }


}
