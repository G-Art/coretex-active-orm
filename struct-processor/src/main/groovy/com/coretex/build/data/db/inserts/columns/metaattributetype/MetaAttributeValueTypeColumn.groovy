package com.coretex.build.data.db.inserts.columns.metaattributetype

import com.coretex.build.data.db.inserts.MetaAttributeTypeInsert
import com.coretex.build.data.db.inserts.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ATTRIBUTE_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_ATTRIBUTE_TYPE, columnName = 'attributeType')
class MetaAttributeValueTypeColumn extends Column<MetaAttributeTypeInsert.AttributeForClassItem> {

    @Override
    Object getRawValue() {
        return data.type.uuid
    }

    @Override
    Object getValue() {
        return "'${getRawValue()}'"
    }
}
