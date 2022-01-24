package com.coretex.build.data.db.updates.columns.regulartype

import com.coretex.build.data.db.diff.dataholders.RegularClassItemDiffDataHolder
import com.coretex.build.data.db.updates.RegularClassTypeUpdate
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = REGULAR_TYPE, columnName = 'regularItemCode')
class RegularTypeRegularItemCodeColumn extends Column<RegularClassItemDiffDataHolder, RegularClassTypeUpdate> {

    @Override
    Object getValue() {
        return "'${data.item.code}'"
    }
}
