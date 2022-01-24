package com.coretex.build.data.db.updates

import com.coretex.build.data.db.diff.dataholders.EnumItemDiffDataHolder
import com.coretex.build.data.db.inserts.MetaEnumTypeInsert
import com.coretex.build.data.db.updates.columns.Column

class MetaEnumTypeUpdate extends Update<EnumItemDiffDataHolder> {

    @Override
    List<String> commands() {
        List<String> commands = []
        if (data.isNew()) {
            def insert = new MetaEnumTypeInsert()
            insert.essentialItem = this.essentialItem
            insert.data = new MetaEnumTypeInsert.EnumRowItem(data.item)
            commands.addAll(insert.commands())
        } else {
            def cols = getColumns()
            if(!cols.isEmpty()){
                commands.add(getQueryFactory().statementQueryFactory().update(essentialItem.tableName, data.itemUUID, cols))
            }
        }

        return commands
    }
}
