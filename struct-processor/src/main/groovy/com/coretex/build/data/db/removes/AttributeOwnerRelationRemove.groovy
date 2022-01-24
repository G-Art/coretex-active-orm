package com.coretex.build.data.db.removes

import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder

class AttributeOwnerRelationRemove extends Remove<AttributeItemDiffDataHolder> {

    @Override
    List<String> commands() {
        List<String> commands = []
        def uuid = data.itemUUID
        commands.add(getQueryFactory().statementQueryFactory().deleteFrom(essentialItem.tableName, Map.<String, String>of("c_source", "'${uuid}'")))
        return commands
    }
}
