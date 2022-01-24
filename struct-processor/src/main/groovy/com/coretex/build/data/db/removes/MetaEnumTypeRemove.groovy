package com.coretex.build.data.db.removes


import com.coretex.build.data.db.diff.dataholders.EnumItemDiffDataHolder

class MetaEnumTypeRemove extends Remove<EnumItemDiffDataHolder> {

    @Override
    List<String> commands() {
        List<String> commands = []
        def uuid = data.itemUUID
        commands.add(getQueryFactory().statementQueryFactory().deleteFrom(essentialItem.tableName, Map.<String, String>of("uuid", "'${uuid}'")))
        return commands
    }
}
