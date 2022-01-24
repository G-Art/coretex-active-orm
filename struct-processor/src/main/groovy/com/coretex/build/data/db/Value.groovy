package com.coretex.build.data.db

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class Value {
    private String value

    Value(value) {
        this.value = value
    }

    String getValue() {
        return value
    }

    void setValue(value) {
        this.value = value
    }
}
