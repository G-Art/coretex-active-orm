package com.coretex.build.data.db.inserts.columns.metaattributetype

import com.coretex.build.data.db.inserts.MetaAttributeTypeInsert
import com.coretex.build.data.db.inserts.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ATTRIBUTE_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_RELATION_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_ATTRIBUTE_TYPE, columnName = 'defaultValue')
class MetaAttributeTypeDefaultValueColumn extends Column<MetaAttributeTypeInsert.AttributeForClassItem> {
    @Override
    Object getRawValue() {
        if (data.item.defaultValue != null) {
            return "'${data.item.defaultValue}'"
        }
        return this.nullValue
    }
}
