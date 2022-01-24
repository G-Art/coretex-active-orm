package com.coretex.build.data.db.inserts

import com.coretex.build.data.items.EnumItem

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class MetaEnumTypeInsert extends Insert<EnumRowItem> {

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

    static class EnumRowItem implements RowItem<EnumItem> {

        private EnumItem enumItem

        EnumRowItem(EnumItem enumItem) {
            this.enumItem = enumItem
        }

        @Override
        EnumItem getItem() {
            return enumItem
        }
    }

}
