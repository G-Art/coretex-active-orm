package com.coretex.build.data.db.updates.columns.metaattributetype

import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.updates.MetaAttributeTypeUpdate
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ATTRIBUTE_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_RELATION_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_ATTRIBUTE_TYPE, columnName = 'columnName')
class MetaAttributeTypeColumnNameColumn extends Column<AttributeItemDiffDataHolder, MetaAttributeTypeUpdate> {

    private final String EXCLUDE_UUID = 'uuid'

    @Override
    Object getValue() {
        if (update.essentialValueType.code != META_RELATION_TYPE.toString() && !data.item.localized) {
            return "'${data.item.name == EXCLUDE_UUID ? data.item.name : data.item.columnName}'"
        }
        return this.nullValue
    }
}
