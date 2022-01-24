package com.coretex.build.data.db.inserts

import com.coretex.build.data.items.EnumItem
import com.coretex.build.data.items.EnumValue

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class MetaEnumValueTypeInsert extends Insert<ValueForEnumItem> {

    @Override
    UUID getUuid() {
        return data.uuid
    }

    @Override
    Collection<String> commands() {
        List<String> commands = []
        commands.add(getQueryFactory().statementQueryFactory().insertInto(this))
        return commands
    }


    static class ValueForEnumItem implements RowItem<EnumValue> {
        private EnumValue item
        private EnumItem enumItem

        ValueForEnumItem(EnumValue enumValue, EnumItem enumItem) {
            this.item = enumValue
            this.enumItem = enumItem
        }

        @Override
        EnumValue getItem() {
            return item
        }

        EnumItem getEnumItem() {
            return enumItem
        }

        UUID getUuid() {
            return item.uuid
        }
    }
}
