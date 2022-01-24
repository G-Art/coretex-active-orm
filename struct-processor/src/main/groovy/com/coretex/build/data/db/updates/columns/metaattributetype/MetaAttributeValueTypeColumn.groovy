package com.coretex.build.data.db.updates.columns.metaattributetype

import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.updates.MetaAttributeTypeUpdate
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ATTRIBUTE_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_ATTRIBUTE_TYPE, columnName = 'attributeType')
class MetaAttributeValueTypeColumn extends Column<AttributeItemDiffDataHolder, MetaAttributeTypeUpdate> {

    @Override
    Object getValue() {
        return "'${update.type.uuid}'"
    }
}
