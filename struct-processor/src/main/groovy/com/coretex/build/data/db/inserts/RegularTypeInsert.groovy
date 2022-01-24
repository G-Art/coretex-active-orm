package com.coretex.build.data.db.inserts

import com.coretex.build.data.items.RegularClassItem
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class RegularTypeInsert extends Insert<RegularTypeWrapper> {

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

    static class RegularTypeWrapper implements RowItem<RegularClassItem> {

        private UUID uuid
        private RegularClassItem regular
        private String name

        RegularTypeWrapper(RegularClassItem item) {
            this.regular = item
            this.name = item.code
            this.uuid = regular.uuid
        }

        @Override
        RegularClassItem getItem() {
            return regular
        }

        String getName() {
            return name
        }

        UUID getUuid() {
            return uuid
        }
    }
}
