package com.coretex.build.data.items


import com.coretex.build.data.items.traits.Insertable
import com.google.common.base.MoreObjects

import static com.coretex.Constants.TABLE_PREFIX
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class Item implements Insertable {

    private String code
    private String description

    Item(String code) {
        this.code = code
    }

    String getCode() {
        code
    }

    String getTableName(){
        return "${TABLE_PREFIX}${code.toLowerCase()}"
    }

    void setCode(String code) {
        this.code = code
    }

    String getDescription() {
        description
    }

    void setDescription(String description) {
        this.description = description
    }

    @Override
    String toString() {
        return MoreObjects.toStringHelper(this)
                .add('code', code)
                .add('description', description)
                .add('uuid', uuid)
                .toString()
    }
}
