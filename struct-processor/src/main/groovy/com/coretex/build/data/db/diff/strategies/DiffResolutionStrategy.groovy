package com.coretex.build.data.db.diff.strategies

import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.items.Item

interface DiffResolutionStrategy<D extends ItemDiffDataHolder<? extends Item>> {

    boolean applicable(D dataHolder)

    Resolution resolve(D dataHolder)
}