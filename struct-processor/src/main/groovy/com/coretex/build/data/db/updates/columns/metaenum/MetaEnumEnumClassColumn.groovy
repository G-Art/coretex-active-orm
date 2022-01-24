package com.coretex.build.data.db.updates.columns.metaenum

import com.coretex.build.data.db.diff.dataholders.EnumItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.EnumValueDiffDataHolder
import com.coretex.build.data.db.updates.MetaEnumTypeUpdate
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_ENUM_TYPE, columnName = 'enumClass')
class MetaEnumEnumClassColumn extends Column<EnumItemDiffDataHolder, MetaEnumTypeUpdate> {

    @Override
    Object getValue() {
        return "'${data.item.fullTypeName}'"
    }
}
