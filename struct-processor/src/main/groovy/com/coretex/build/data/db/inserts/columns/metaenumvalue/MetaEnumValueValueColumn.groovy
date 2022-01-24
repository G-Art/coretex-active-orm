package com.coretex.build.data.db.inserts.columns.metaenumvalue

import com.coretex.build.data.db.inserts.MetaEnumValueTypeInsert
import com.coretex.build.data.db.inserts.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_VALUE_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_ENUM_VALUE_TYPE, columnName = 'value')
class MetaEnumValueValueColumn extends Column<MetaEnumValueTypeInsert.ValueForEnumItem> {

    @Override
    Object getRawValue() {
        return "'${data.item.value}'"
    }
}
