package com.coretex.build.data.db.inserts.columns.metaenum

import com.coretex.build.data.db.inserts.MetaEnumTypeInsert
import com.coretex.build.data.db.inserts.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_ENUM_TYPE, columnName = 'enumClass')
class MetaEnumEnumClassColumn extends Column<MetaEnumTypeInsert.EnumRowItem> {

    @Override
    Object getRawValue() {
        return "'${data.item.fullTypeName}'"
    }
}
