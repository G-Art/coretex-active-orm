package com.coretex.build.data.db.updates.columns.metatype


import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.updates.Update
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.RelationItem
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */

@EssentialDataItemColumn(itemCode = META_TYPE, columnName = 'tableName')
class MetaTypeTableNameColumn extends Column<AbstractClassItemDiffDataHolder, Update> {

    @Override
    Object getValue() {
        return "'${getTableName(data.item as AbstractItem)}'"
    }

    String getTableName(AbstractItem item) {
        if (item.class in RelationItem) {
            return (item as RelationItem).tableName
        }
        if (item.table) {
            return item.tableName
        }
        return getTableName(item.parentItem)
    }
}
