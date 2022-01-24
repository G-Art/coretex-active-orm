package com.coretex.build.data.db.updates


import com.coretex.build.data.db.diff.dataholders.RegularClassItemDiffDataHolder
import com.coretex.build.data.db.inserts.MetaTypeInsert
import com.coretex.build.data.db.inserts.RegularTypeInsert
import com.coretex.build.data.db.removes.Remove
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.build.data.items.AbstractItem

class RegularClassTypeUpdate extends Update<RegularClassItemDiffDataHolder> {

    @Override
    List<String> commands() {
        List<String> commands = []
        if (data.isNew()) {
            def insert = new RegularTypeInsert()
            insert.essentialItem = this.essentialItem
            insert.data = new RegularTypeInsert.RegularTypeWrapper(data.item)
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
