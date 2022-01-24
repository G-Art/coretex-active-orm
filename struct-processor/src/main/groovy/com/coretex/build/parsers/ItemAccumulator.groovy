package com.coretex.build.parsers

import com.coretex.build.data.items.AbstractItem

class ItemAccumulator {

    private final List<AbstractItem> items = new ArrayList<>()

    void addItem(AbstractItem item) {
        items.add(item)
    }

    List<AbstractItem> getItems() {
        return items
    }

    boolean hasItems() {
        return !items.isEmpty()
    }
}
