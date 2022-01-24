package com.coretex.build.data.db.updates.columns.metatype


import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.updates.Update
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_TYPE, columnName = 'tableOwner')
class MetaTypeTableOwnerColumn extends Column<AbstractClassItemDiffDataHolder, Update> {

    @Override
    Object getValue() {
        return "${data.item.table ? 'TRUE' : 'FALSE'}"
    }
}
