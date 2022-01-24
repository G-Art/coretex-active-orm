
package com.coretex.items.core;

/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 2021-12-20 18:56:29
* ----------------------------------------------------------------
*/

import com.coretex.orm.core.services.items.context.ItemContext;


/**
 * Regular item
 */
public class RegularTypeItem extends GenericItem {

    public static final String ITEM_TYPE = "RegularType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : dbType
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : RegularTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String DB_TYPE = "dbType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : regularItemCode
    *                        @optional      : false
    *                        @unique        : true
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : RegularTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String REGULAR_ITEM_CODE = "regularItemCode";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : regularClass
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : RegularTypeItem
    *                        @type          : Class
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String REGULAR_CLASS = "regularClass";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : persistenceType
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : RegularTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String PERSISTENCE_TYPE = "persistenceType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : columnSize
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : RegularTypeItem
    *                        @type          : Integer
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String COLUMN_SIZE = "columnSize";

    public RegularTypeItem() {
        super();
    }

    public RegularTypeItem(ItemContext ctx) {
        super(ctx);
    }
    /**
    * Sql type representation
    */
    public String getDbType(){
        return getItemContext().getValue(DB_TYPE);
    }

    /**
    * Sql type representation
    */
    public void setDbType(String dbType){
        getItemContext().setValue(DB_TYPE, dbType);
    }

    /**
    * Java type representation
    */
    public String getRegularItemCode(){
        return getItemContext().getValue(REGULAR_ITEM_CODE);
    }

    /**
    * Java type representation
    */
    public void setRegularItemCode(String regularItemCode){
        getItemContext().setValue(REGULAR_ITEM_CODE, regularItemCode);
    }

    /**
    * Java class type representation
    */
    public Class getRegularClass(){
        return getItemContext().getValue(REGULAR_CLASS);
    }

    /**
    * Java class type representation
    */
    public void setRegularClass(Class regularClass){
        getItemContext().setValue(REGULAR_CLASS, regularClass);
    }

    /**
    * Persistence value qualifier
    */
    public String getPersistenceType(){
        return getItemContext().getValue(PERSISTENCE_TYPE);
    }

    /**
    * Persistence value qualifier
    */
    public void setPersistenceType(String persistenceType){
        getItemContext().setValue(PERSISTENCE_TYPE, persistenceType);
    }

    /**
    * Persistence value qualifier
    */
    public Integer getColumnSize(){
        return getItemContext().getValue(COLUMN_SIZE);
    }

    /**
    * Persistence value qualifier
    */
    public void setColumnSize(Integer columnSize){
        getItemContext().setValue(COLUMN_SIZE, columnSize);
    }

}






