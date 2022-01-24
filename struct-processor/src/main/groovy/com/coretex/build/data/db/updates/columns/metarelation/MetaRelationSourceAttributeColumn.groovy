package com.coretex.build.data.db.updates.columns.metarelation

import com.coretex.build.data.db.diff.dataholders.RelationItemDiffDataHolder
import com.coretex.build.data.db.inserts.MetaTypeInsert
import com.coretex.build.data.db.updates.Update
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.build.data.items.RelationItem
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_TYPE, supportedItem = RelationItem, columnName = 'sourceAttribute')
class MetaRelationSourceAttributeColumn extends Column<RelationItemDiffDataHolder, Update> {

    @Override
    Object getValue() {
        return "'${data.item.sourceAttribute.uuid}'"
    }
}
