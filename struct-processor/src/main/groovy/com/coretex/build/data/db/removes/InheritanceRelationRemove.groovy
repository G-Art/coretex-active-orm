package com.coretex.build.data.db.removes

import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder

class InheritanceRelationRemove extends Remove<AbstractClassItemDiffDataHolder> {

    @Override
    List<String> commands() {
        List<String> commands = []
        def uuid = data.itemUUID
        commands.add(getQueryFactory().statementQueryFactory().deleteFrom(essentialItem.tableName, Map.<String, String>of("c_source", "'${uuid}'")))
        return commands
    }
}
