package com.coretex.build.data.db.diff.strategies

import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.items.Item

abstract class Resolution {
    ItemDiffDataHolder<Item> itemDiffDataHolder

    List<String> commands = []

    @Override
    String toString() {
        return "${this.getClass().getSimpleName()} {itemDiffDataHolder = ${itemDiffDataHolder}, commands = ${commands.size()} }"
    }
}
