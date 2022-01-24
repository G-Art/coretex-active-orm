package com.coretex.build.services.impl.collectors


import com.coretex.build.data.db.diff.dataholders.RegularClassItemDiffDataHolder
import com.coretex.build.data.items.RegularClassItem
import com.coretex.build.services.QueryFactory
import org.apache.commons.collections4.CollectionUtils

import java.sql.Connection

import static com.coretex.common.utils.BuildUtils.execute

class RegularClassItemDiffDataCollector implements ItemDiffDataCollector<RegularClassItem> {

    private QueryFactory queryFactory

    RegularClassItemDiffDataCollector(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    Optional<RegularClassItemDiffDataHolder> collect(RegularClassItem item, Connection con) {
        return Optional.of(new RegularClassItemDiffDataHolder(item: item))
                .map({
                    con.createStatement().withCloseable {statement ->
                        def res = execute(queryFactory.statementQueryFactory().selectRegularItem(item), statement)
                        if (CollectionUtils.isNotEmpty(res)) {
                            assert res.size() == 1: "Regular item [${item.code}] has ambigous db records"
                            def map = res.iterator().next()
                            it.item.uuid = map.get("uuid") as UUID
                            it.dbData = map
                        }
                        return it
                    }
                    return it
                })
    }
}