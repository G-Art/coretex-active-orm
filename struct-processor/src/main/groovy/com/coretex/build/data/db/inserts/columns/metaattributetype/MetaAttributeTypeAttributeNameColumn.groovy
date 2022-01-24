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
@EssentialDataItemColumn(itemCode = META_ATTRIBUTE_TYPE, columnName = 'attributeName')
class MetaAttributeTypeAttributeNameColumn extends Column<MetaAttributeTypeInsert.AttributeForClassItem> {

    @Override
    Object getRawValue() {
        return data.item.name
    }

    @Override
    Object getValue() {
        return "'${getRawValue()}'"
    }
}
