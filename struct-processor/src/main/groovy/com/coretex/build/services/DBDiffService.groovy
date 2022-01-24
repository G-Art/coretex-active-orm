package com.coretex.build.services


import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.RegularClassItem

import java.sql.Connection
import java.util.stream.Stream

abstract class DBDiffService {

    abstract Stream<ItemDiffDataHolder> defineItemDiffHolder(Connection connection)
    abstract Resolution resolve(ItemDiffDataHolder<Item> dataHolder)


}
