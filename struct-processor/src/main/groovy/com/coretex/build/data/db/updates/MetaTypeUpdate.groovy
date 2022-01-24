package com.coretex.build.data.db.updates

import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.inserts.MetaTypeInsert
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.build.data.items.AbstractItem

class MetaTypeUpdate extends Update<AbstractClassItemDiffDataHolder> {

    @Override
    List<String> commands() {
        List<String> commands = []
        if (data.isNew()) {
            def metaTypeInsert = new MetaTypeInsert()
            metaTypeInsert.essentialItem = this.essentialItem
            metaTypeInsert.data = new MetaTypeInsert.MetaTypeRowItem(data.item as AbstractItem)
            commands.addAll(metaTypeInsert.commands())
        } else {
            def cols = getColumns()
            if(!cols.isEmpty()){
                commands.add(getQueryFactory().statementQueryFactory().update(essentialItem.tableName, data.itemUUID, cols))
            }
        }

        return commands
    }
}
