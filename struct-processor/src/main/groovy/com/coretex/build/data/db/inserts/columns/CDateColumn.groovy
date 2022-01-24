package com.coretex.build.data.db.inserts.columns

import com.coretex.build.data.db.inserts.RowItem
import com.coretex.build.data.items.Item
import com.coretex.common.annotation.EssentialDataItemColumn

import java.sql.Timestamp
import java.time.Clock

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */

@EssentialDataItemColumn(columnName = 'createDate')
class CDateColumn extends Column<RowItem<? extends Item>> {

    @Override
    Object getRawValue() {
        return "'${new Timestamp(Clock.systemDefaultZone().millis())}'"
    }
}
