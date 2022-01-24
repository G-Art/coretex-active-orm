package com.coretex.build.services.impl.postgres

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.DiffResolutionStrategy
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.data.db.diff.strategies.creating.CreateItemResolutionStrategy
import com.coretex.build.data.db.diff.strategies.removing.RemoveItemResolutionStrategy
import com.coretex.build.data.db.diff.strategies.updating.UpdateItemResolutionStrategy
import com.coretex.build.data.items.Item
import com.coretex.build.services.DBDiffService
import com.coretex.build.services.QueryFactory
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import java.sql.Connection
import java.util.stream.Collectors
import java.util.stream.Stream

class PostgresqlDBDiffService extends DBDiffService {

    private Logger LOG = Logging.getLogger(PostgresqlDBDiffService)
    private CoretexPluginContext context = CoretexPluginContext.instance

    PostgresqlDiffMetaExtractor metaExtractor;

    List<DiffResolutionStrategy> diffResolutionStrategies
    private QueryFactory queryFactory

    PostgresqlDBDiffService(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
        metaExtractor = new PostgresqlDiffMetaExtractor(queryFactory)
        diffResolutionStrategies = [
                new RemoveItemResolutionStrategy(queryFactory),
                new UpdateItemResolutionStrategy(queryFactory),
                new CreateItemResolutionStrategy(queryFactory)
        ]
    }

    @Override
    Stream<ItemDiffDataHolder> defineItemDiffHolder(Connection con) {
        def itemsDiffs = Stream.concat(collectMetaDatas(context.items, con), collectMetaDatas(context.regularItems.values() as List<Item>, con))
                .collect(Collectors.toList())
        return Stream.concat(metaExtractor.collectRemovedItemsMetaData(itemsDiffs, con), itemsDiffs.stream())
    }

    Stream<ItemDiffDataHolder<? extends Item>> collectMetaDatas(List<Item> items, Connection con){
        return items.stream()
                .map({metaExtractor.collectMetaData(it, con)})
                .flatMap({it.stream()})
    }

    @Override
    Resolution resolve(ItemDiffDataHolder<Item> dataHolder) {
        return diffResolutionStrategies.find {
            it.applicable(dataHolder)
        }?.resolve(dataHolder)
    }

}
