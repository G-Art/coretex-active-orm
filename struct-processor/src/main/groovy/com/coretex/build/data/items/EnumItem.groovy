package com.coretex.build.data.items


import com.coretex.build.data.items.traits.Adjustable
import com.coretex.struct.StructConstants

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class EnumItem extends AbstractItem implements Adjustable {

    private Set<EnumValue> values

    EnumItem(String code) {
        super(code)
        values = [] as Set
    }

    Set<EnumValue> getValues() {
        values
    }

    void setValues(Set<EnumValue> values) {
        values.each {
            addValue(it)
        }
    }

    void addValue(EnumValue value) {
        if (value != null) {
            value.owner = this
            values.add(value)
        }
    }

    String getTypeName() {
        "${code}${StructConstants.Enum.CLASS_SUFFIX}"
    }

    @Override
    String getItemTypeFolder() {
        return 'enums'
    }
}
