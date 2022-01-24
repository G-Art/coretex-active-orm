
package com.coretex.items.core;

/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 2021-12-20 18:56:29
* ----------------------------------------------------------------
*/

import com.coretex.orm.core.services.items.context.ItemContext;

import java.util.Set;

/**
 * Enum type holds information about java enum type
 */
public class MetaEnumTypeItem extends GenericItem {

    public static final String ITEM_TYPE = "MetaEnumType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : enumClass
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaEnumTypeItem
    *                        @type          : Class
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String ENUM_CLASS = "enumClass";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : enumCode
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaEnumTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String ENUM_CODE = "enumCode";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : values
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaEnumTypeItem
    *                        @type          : com.coretex.items.core.MetaEnumValueTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String VALUES = "values";

    public MetaEnumTypeItem() {
        super();
    }

    public MetaEnumTypeItem(ItemContext ctx) {
        super(ctx);
    }
    /**
    * Enum class name
    */
    public Class getEnumClass(){
        return getItemContext().getValue(ENUM_CLASS);
    }

    /**
    * Enum class name
    */
    public void setEnumClass(Class enumClass){
        getItemContext().setValue(ENUM_CLASS, enumClass);
    }

    /**
    * Enum code
    */
    public String getEnumCode(){
        return getItemContext().getValue(ENUM_CODE);
    }

    /**
    * Enum code
    */
    public void setEnumCode(String enumCode){
        getItemContext().setValue(ENUM_CODE, enumCode);
    }

    /**
    * Enum values
    */
    public Set<MetaEnumValueTypeItem> getValues(){
        return getItemContext().getValue(VALUES);
    }

    /**
    * Enum values
    */
    public void setValues(Set<MetaEnumValueTypeItem> values){
        getItemContext().setValue(VALUES, values);
    }

}






