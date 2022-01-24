package com.coretex.build.data.db.inserts.columns.metatype

import com.coretex.build.data.db.inserts.MetaTypeInsert
import com.coretex.build.data.db.inserts.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_TYPE, columnName = 'description')
class MetaTypeTypeDescriptionColumn extends Column<MetaTypeInsert.MetaTypeRowItem> {

    @Override
    Object getRawValue() {
        return data.item.description ? "'${data.item.description}'" : nullValue
    }
}
