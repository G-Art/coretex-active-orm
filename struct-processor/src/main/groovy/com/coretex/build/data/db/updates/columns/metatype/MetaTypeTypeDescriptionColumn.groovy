package com.coretex.build.data.db.updates.columns.metatype


import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.updates.Update
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItemColumn
import org.apache.commons.lang.StringEscapeUtils

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = META_TYPE, columnName = 'description')
class MetaTypeTypeDescriptionColumn extends Column<AbstractClassItemDiffDataHolder, Update> {

    @Override
    Object getValue() {
        return data.item.description ? "'${StringEscapeUtils.escapeSql(data.item.description)}'" : nullValue
    }
}
