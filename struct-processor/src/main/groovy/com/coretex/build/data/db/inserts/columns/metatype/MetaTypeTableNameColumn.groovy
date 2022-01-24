package com.coretex.build.data.db.inserts.columns.metatype

import com.coretex.build.data.db.inserts.MetaTypeInsert
import com.coretex.build.data.db.inserts.columns.Column
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
class MetaTypeTableNameColumn extends Column<MetaTypeInsert.MetaTypeRowItem> {

    @Override
    Object getRawValue() {
        return "'${getTableName(data.item)}'"
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
