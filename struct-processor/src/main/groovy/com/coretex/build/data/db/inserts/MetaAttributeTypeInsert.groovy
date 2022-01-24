package com.coretex.build.data.db.inserts

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.attributes.Attribute

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class MetaAttributeTypeInsert extends Insert<AttributeForClassItem> {

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

    static class AttributeForClassItem implements RowItem<Attribute<Item>> {
        private Attribute<Item> attribute
        private ClassItem essentialValueType
        private Item type

        AttributeForClassItem(Attribute<Item> attribute, ClassItem essentialValueType, Item type) {
            this.type = type
            this.attribute = attribute
            this.essentialValueType = essentialValueType
        }

        Item getType() {
            return type
        }

        @Override
        Attribute<Item> getItem() {
            return attribute
        }

        ClassItem getEssentialValueType() {
            return essentialValueType
        }

        UUID getUuid() {
            return attribute.uuid
        }
    }
}
