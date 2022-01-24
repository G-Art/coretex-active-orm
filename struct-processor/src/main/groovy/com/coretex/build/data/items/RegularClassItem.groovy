package com.coretex.build.data.items

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class RegularClassItem extends Item {

    private String shortCode
    private Boolean ignored
    private Class regularClass

    RegularClassItem(Class regularClass) {
        super(regularClass.name)
        this.ignored = regularClass.package?.name == 'java.lang'
        this.shortCode = regularClass.simpleName
        this.regularClass = regularClass
    }

    Class getRegularClass() {
        return regularClass
    }

    void setRegularClass(Class regularClass) {
        this.regularClass = regularClass
    }

    String getShortCode() {
        return shortCode
    }

    void setShortCode(String shortCode) {
        this.shortCode = shortCode
    }

    Boolean getIgnored() {
        return ignored
    }

    void setIgnored(Boolean ignored) {
        this.ignored = ignored
    }
}
