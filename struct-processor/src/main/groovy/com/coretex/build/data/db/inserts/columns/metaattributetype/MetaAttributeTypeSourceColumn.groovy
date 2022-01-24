package com.coretex.build.data.db.inserts.columns.metaattributetype

import com.coretex.build.data.db.inserts.MetaAttributeTypeInsert
import com.coretex.build.data.db.inserts.columns.Column
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RelationItem
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ATTRIBUTE_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_ATTRIBUTE_TYPE, columnName = 'source')
class MetaAttributeTypeSourceColumn extends Column<MetaAttributeTypeInsert.AttributeForClassItem> {

    @Override
    Object getRawValue() {
        AbstractItem owner = data.item.owner
        if (owner) {
            if (owner.class in ClassItem) {
                RelationItem relation = (owner as ClassItem).relations[data.item.name]
                if (relation) {
                    return relation.sourceAttribute.name == data.item.name ? 'true' : 'false'
                }
            } else if (owner.class in RelationItem) {
                return 'true'
            }
        }
        return 'false'
    }
}
