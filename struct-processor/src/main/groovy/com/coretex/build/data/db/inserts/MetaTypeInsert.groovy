package com.coretex.build.data.db.inserts

import com.coretex.build.data.items.AbstractItem
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class MetaTypeInsert extends Insert<MetaTypeRowItem> {

    @Override
    UUID getUuid() {
        return data.item.uuid
    }

    @Override
    Collection<String> commands() {
        List<String> commands = []
        commands.add(getQueryFactory().statementQueryFactory().insertInto(this))
        return commands
    }

    static class MetaTypeRowItem implements RowItem<AbstractItem> {

        private AbstractItem abstractItem

        MetaTypeRowItem(AbstractItem abstractItem) {
            this.abstractItem = abstractItem
        }

        @Override
        AbstractItem getItem() {
            return abstractItem
        }
    }

}
