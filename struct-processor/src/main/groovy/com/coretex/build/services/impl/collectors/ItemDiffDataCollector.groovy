package com.coretex.build.services.impl.collectors

import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.items.Item

import java.sql.Connection

interface ItemDiffDataCollector<I extends Item> {

    Optional<ItemDiffDataHolder<I>> collect(I item, Connection connection)

}