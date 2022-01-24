package com.coretex.build.data.items
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class EnumValue extends Item {

    private String value
    private EnumItem owner

    EnumValue(String value) {
        super(value)
        this.value = value
    }

    String getValue() {
        return value
    }

    void setValue(value) {
        this.value = value
    }

    EnumItem getOwner() {
        return owner
    }

    void setOwner(EnumItem owner) {
        this.owner = owner
    }
}
