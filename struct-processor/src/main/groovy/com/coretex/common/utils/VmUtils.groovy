package com.coretex.common.utils

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.RegularClassItem
import com.coretex.build.data.items.attributes.Attribute

import javax.lang.model.SourceVersion

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class VmUtils {

    static String typeForRelation(Attribute<ClassItem> attribute) {
        if (attribute.containerType) {
            return "${attribute.containerType.simpleName}<$attribute.type.typeName>"
        }
        return attribute.type.typeName
    }

    boolean isBooleanType(Attribute<Item> inType) {
        return isRegularType(inType.type) && inType.type.regularClass == Boolean
    }

    static boolean isUUIDType(Object o) {
        return o instanceof UUID
    }

    static String toUpperCaseWithUnderline(String text) {
        return text.replaceAll('(.)([A-Z])', '$1_$2').toUpperCase()
    }

    static Boolean isRegularType(Item item) {
        return item.class == RegularClassItem
    }

    static String sanitizeKeyword(String text){
        if(SourceVersion.isKeyword(text)){
            return "_${text}"
        }
        return text
    }
}
