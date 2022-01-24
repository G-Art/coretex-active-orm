
package com.coretex.items.core;

/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 2021-12-20 18:56:29
* ----------------------------------------------------------------
*/

import com.coretex.orm.core.services.items.context.ItemContext;


/**
 * Enum type holds information about java enum value 
 */
public class MetaEnumValueTypeItem extends GenericItem {

    public static final String ITEM_TYPE = "MetaEnumValueType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : code
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaEnumValueTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String CODE = "code";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : value
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaEnumValueTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String VALUE = "value";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : owner
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaEnumValueTypeItem
    *                        @type          : com.coretex.items.core.MetaEnumTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String OWNER = "owner";

    public MetaEnumValueTypeItem() {
        super();
    }

    public MetaEnumValueTypeItem(ItemContext ctx) {
        super(ctx);
    }
    /**
    * Enum instance code
    */
    public String getCode(){
        return getItemContext().getValue(CODE);
    }

    /**
    * Enum instance code
    */
    public void setCode(String code){
        getItemContext().setValue(CODE, code);
    }

    /**
    * Enum instance value
    */
    public String getValue(){
        return getItemContext().getValue(VALUE);
    }

    /**
    * Enum instance value
    */
    public void setValue(String value){
        getItemContext().setValue(VALUE, value);
    }

    /**
    * Enum value owner
    */
    public MetaEnumTypeItem getOwner(){
        return getItemContext().getValue(OWNER);
    }

    /**
    * Enum value owner
    */
    public void setOwner(MetaEnumTypeItem owner){
        getItemContext().setValue(OWNER, owner);
    }

}






