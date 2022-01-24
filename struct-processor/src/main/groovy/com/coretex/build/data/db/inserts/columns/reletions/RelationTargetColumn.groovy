package com.coretex.build.data.db.inserts.columns.reletions

import com.coretex.build.data.db.inserts.RelationInsert
import com.coretex.build.data.db.inserts.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_RELATION_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_RELATION_TYPE, columnName = 'target')
class RelationTargetColumn extends Column<RelationInsert.RelationData> {

    @Override
    Object getRawValue() {
        return data.target.uuid
    }

    @Override
    Object getValue() {
        return "'${getRawValue()}'"
    }
}
