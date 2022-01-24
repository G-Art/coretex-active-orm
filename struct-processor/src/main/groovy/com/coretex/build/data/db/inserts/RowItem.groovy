package com.coretex.build.data.db.inserts

import com.coretex.build.data.items.Item

interface RowItem<T extends Item> {

    T getItem()
}
