package com.coretex.build.data.items.traits

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
trait DbEntity {

    private boolean table = true

    void setTable(Boolean table) {
        this.table = table
    }

    boolean getTable() {
        return table
    }

}