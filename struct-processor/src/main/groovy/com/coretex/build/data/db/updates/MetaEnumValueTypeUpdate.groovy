package com.coretex.build.data.db.updates


import com.coretex.build.data.db.diff.dataholders.EnumValueDiffDataHolder
import com.coretex.build.data.db.inserts.MetaEnumValueTypeInsert
import com.coretex.build.data.db.removes.MetaEnumValueTypeRemove

class MetaEnumValueTypeUpdate extends Update<EnumValueDiffDataHolder> {

    @Override
    List<String> commands() {
        List<String> commands = []
        if (data.isNew()) {
            def insert = new MetaEnumValueTypeInsert()
            insert.essentialItem = this.essentialItem
            insert.data = new MetaEnumValueTypeInsert.ValueForEnumItem(data.item, data.item.owner)
            commands.addAll(insert.commands())
        }

        if(data.isRemoved()){
            def remove = new MetaEnumValueTypeRemove()
            remove.essentialItem = this.essentialItem
            remove.data = data
            commands.addAll(remove.commands())
        }

        return commands
    }
}
