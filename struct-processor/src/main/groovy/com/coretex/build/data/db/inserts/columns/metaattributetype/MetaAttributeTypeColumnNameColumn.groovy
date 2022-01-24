package com.coretex.build.data.db.inserts.columns.metaattributetype

import com.coretex.build.data.db.inserts.MetaAttributeTypeInsert
import com.coretex.build.data.db.inserts.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ATTRIBUTE_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_RELATION_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_ATTRIBUTE_TYPE, columnName = 'columnName')
class MetaAttributeTypeColumnNameColumn extends Column<MetaAttributeTypeInsert.AttributeForClassItem> {

    private final String EXCLUDE_UUID = 'uuid'

    @Override
    Object getRawValue() {
        if (data.essentialValueType.code != META_RELATION_TYPE.toString() && !data.item.localized) {
            return "'${data.item.name == EXCLUDE_UUID ? data.item.name : data.item.columnName}'"
        }
        return this.nullValue
    }
}
