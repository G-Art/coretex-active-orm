package com.coretex.build.data.db.removes

import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.ClassItemDiffDataHolder

class MetaTypeRemove extends Remove<AbstractClassItemDiffDataHolder> {

    @Override
    List<String> commands() {
        List<String> commands = []
        def uuid = data.itemUUID
        commands.add(getQueryFactory().statementQueryFactory().deleteFrom(essentialItem.tableName, Map.<String, String>of("uuid", "'${uuid}'")))
        return commands
    }
}
