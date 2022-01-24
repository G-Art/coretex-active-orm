package com.coretex.build.data.db.updates.columns.metaattributetype

import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.updates.MetaAttributeTypeUpdate
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn
import org.apache.commons.lang.StringEscapeUtils

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ATTRIBUTE_TYPE
import static org.apache.commons.lang3.StringUtils.isNotBlank
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_ATTRIBUTE_TYPE, columnName = 'description')
class MetaAttributeTypeDescriptionColumn extends Column<AttributeItemDiffDataHolder, MetaAttributeTypeUpdate> {

    @Override
    Object getValue() {
        return isNotBlank(data.item.description) ?
                "'${StringEscapeUtils.escapeSql(data.item.description)}'" : nullValue
    }
}
