package com.coretex.build.data.db.removes


import com.coretex.build.data.db.diff.dataholders.RegularClassItemDiffDataHolder

class RegularClassTypeRemove extends Remove<RegularClassItemDiffDataHolder> {

    @Override
    List<String> commands() {
        List<String> commands = []
        def uuid = data.itemUUID
        commands.add(getQueryFactory().statementQueryFactory().deleteFrom(essentialItem.tableName, Map.<String, String>of("uuid", "'${uuid}'")))
        return commands
    }
}
