package com.coretex.build.services.impl.collectors

import com.coretex.build.data.db.diff.dataholders.EnumValueDiffDataHolder
import com.coretex.build.data.items.EnumValue
import com.coretex.build.services.QueryFactory
import org.apache.commons.collections4.CollectionUtils

import java.sql.Connection

import static com.coretex.common.utils.BuildUtils.execute

class EnumValueDiffDataCollector implements ItemDiffDataCollector<EnumValue> {

    private QueryFactory queryFactory

    EnumValueDiffDataCollector(QueryFactory queryFactory) {
        this.queryFactory = queryFactory
    }

    @Override
    Optional<EnumValueDiffDataHolder> collect(EnumValue item, Connection con) {
        return Optional.of(new EnumValueDiffDataHolder(item: item))
                .map({
                    con.createStatement().withCloseable { statement ->
                        def res = execute(queryFactory.statementQueryFactory()
                                .selectEnumValueTypeByUUIDAndCodeAndValue(item.owner.uuid.toString(), item.code, item.value), statement)
                        if (CollectionUtils.isNotEmpty(res)) {
                            assert res.size() == 1: "Enum value item [${item.owner.code}::${item.code}] has ambigous db records"
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