package com.coretex.build.services.impl.collectors

import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.RelationItemDiffDataHolder
import com.coretex.build.data.items.RelationItem
import com.coretex.build.services.QueryFactory
import org.apache.commons.collections4.CollectionUtils

import java.sql.Connection
import java.util.stream.Collectors

import static com.coretex.common.utils.BuildUtils.execute

class RelationItemDiffDataCollector implements ItemDiffDataCollector<RelationItem> {
    private QueryFactory queryFactory

    def attributeDiffDataProcessor = new AttributeDiffDataCollector()

    RelationItemDiffDataCollector(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    Optional<RelationItemDiffDataHolder> collect(RelationItem item, Connection con) {

        return Optional.of(new RelationItemDiffDataHolder(item: item))
                .map({
                    con.createStatement().withCloseable {statement ->
                        def res = execute(queryFactory.statementQueryFactory().selectClassItemByCodeAndType(item), statement)
                        if (CollectionUtils.isNotEmpty(res)) {
                            assert res.size() == 1: "Class item [${item.code}] has ambigous db records"
                            def map = res.iterator().next()
                            it.item.uuid = map.get("uuid") as UUID
                            it.dbData = map
                        }
                        return it
                    }

                    it.dataHolderMap = it.item.implicitAttributes
                            .stream()
                            .map({
                                return attributeDiffDataProcessor.collect(it, con)
                            })
                            .flatMap({it.stream()})
                            .collect(Collectors.toMap({(it.itemUUID)},{it}))

                    return it
                })
    }
}