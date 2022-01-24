package com.coretex.common.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface EssentialDataItem {

    EssentialItem itemCode()

    enum EssentialItem {

        FOR_ALL(''),// used for generic type columns

        META_TYPE('MetaType'),
        META_ENUM_TYPE('MetaEnumType'),
        META_ENUM_VALUE_TYPE('MetaEnumValueType'),
        META_ATTRIBUTE_TYPE('MetaAttributeType'),
        META_RELATION_TYPE('MetaRelationType'),
        INHERITANCE_RELATION('MetaTypeInheritanceRelation'),
        ATTRIBUTE_OWNER_RELATION('MetaAttributeOwnerRelation'),
        ENUM_VALUE_OWNER_RELATION('MetaEnumValueOwnerRelation'),
        REGULAR_TYPE('RegularType')

        private String itemTypeCode

        EssentialItem(String itemTypeCode) {
            this.itemTypeCode = itemTypeCode
        }

        @Override
        String toString() {
            return itemTypeCode
        }

        static EssentialItem fromString(String itemTypeCode) {

            for (EssentialItem state : values()) {
                if (state.itemTypeCode == itemTypeCode) {
                    return state
                }
            }

            throw new NoSuchElementException("Element with code [${itemTypeCode}] does not exist")
        }
    }
}