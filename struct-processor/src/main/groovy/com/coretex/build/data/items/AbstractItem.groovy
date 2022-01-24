package com.coretex.build.data.items

import com.google.common.base.MoreObjects

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
abstract class AbstractItem extends Item {

    private String ownerModuleName
    private String pkg

    AbstractItem(String code) {
        super(code)
    }

    abstract String getTypeName()

    String getFullTypeName() {
        return "${pkg}.${typeName}"
    }

    String getOwnerModuleName() {
        ownerModuleName
    }

    void setOwnerModuleName(String ownerModuleName) {
        this.ownerModuleName = ownerModuleName
    }

    String getPackage() {
        pkg
    }

    void setPackage(String pkg) {
        this.pkg = pkg
    }

    @Override
    String toString() {
        return MoreObjects.toStringHelper(this)
                .add('ownerModuleName', ownerModuleName)
                .add('pkg', pkg)
                .toString()
    }

    abstract String getItemTypeFolder()
}
