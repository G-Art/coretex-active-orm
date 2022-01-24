package com.coretex.build.services.impl.collectors

import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.items.Item

import java.sql.Connection
import java.util.stream.Stream

interface RemovedItemDiffDataCollector<H extends ItemDiffDataHolder<? extends Item>> {
    Stream<H> collect(List<ItemDiffDataHolder<? extends Item>> existHolders, Connection connection)

}