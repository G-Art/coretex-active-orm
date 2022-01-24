package com.coretex.build.data.db.updates


import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.inserts.MetaAttributeTypeInsert
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.attributes.Attribute

class MetaAttributeTypeUpdate extends Update<AttributeItemDiffDataHolder> {

    Item type
    Attribute<Item> attribute
    ClassItem essentialValueType

    @Override
    List<String> commands() {
        List<String> commands = []

        if(data.isNew()){
            def insert = new MetaAttributeTypeInsert()
            insert.essentialItem = this.essentialItem
            insert.data = new MetaAttributeTypeInsert.AttributeForClassItem(attribute, essentialValueType, type)
            commands.addAll(insert.commands())
        }else{
            def diffCol = getColumns()
            if (!diffCol.isEmpty()) {
                commands.add(getQueryFactory().statementQueryFactory().update(essentialItem.tableName, data.itemUUID, diffCol))
            }
        }

        return commands
    }

}
