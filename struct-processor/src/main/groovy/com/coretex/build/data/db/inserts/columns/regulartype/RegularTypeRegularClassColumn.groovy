package com.coretex.build.data.db.inserts.columns.regulartype

import com.coretex.build.data.db.inserts.RegularTypeInsert
import com.coretex.build.data.db.inserts.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = REGULAR_TYPE, columnName = 'regularClass')
class RegularTypeRegularClassColumn extends Column<RegularTypeInsert.RegularTypeWrapper> {

    @Override
    Object getRawValue() {
        return "'${data.item.regularClass.name}'"
    }
}
