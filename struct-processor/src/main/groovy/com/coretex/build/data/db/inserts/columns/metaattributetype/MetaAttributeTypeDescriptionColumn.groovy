package com.coretex.build.data.db.inserts.columns.metaattributetype

import com.coretex.build.data.db.inserts.MetaAttributeTypeInsert
import com.coretex.build.data.db.inserts.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ATTRIBUTE_TYPE
import static org.apache.commons.lang.StringUtils.isNotBlank
import static org.apache.commons.lang3.RegExUtils.replaceAll

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_ATTRIBUTE_TYPE, columnName = 'description')
class MetaAttributeTypeDescriptionColumn extends Column<MetaAttributeTypeInsert.AttributeForClassItem> {

    @Override
    Object getRawValue() {
        return isNotBlank(data.item.description) ?
                "'${replaceAll(data.item.description, '\'', '\'\'')}'" : nullValue
    }

}
