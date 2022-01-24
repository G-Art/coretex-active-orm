
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
 * Meta information of item
 */
public class MetaTypeItem extends GenericItem {

    public static final String ITEM_TYPE = "MetaType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : typeCode
    *                        @optional      : false
    *                        @unique        : true
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String TYPE_CODE = "typeCode";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : itemClass
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaTypeItem
    *                        @type          : Class
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String ITEM_CLASS = "itemClass";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : tableName
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String TABLE_NAME = "tableName";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : tableOwner
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  : true
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaTypeItem
    *                        @type          : Boolean
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String TABLE_OWNER = "tableOwner";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : description
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String DESCRIPTION = "description";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : abstract
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaTypeItem
    *                        @type          : Boolean
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String ABSTRACT = "abstract";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : parent
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaTypeItem
    *                        @type          : com.coretex.items.core.MetaTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String PARENT = "parent";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : subtypes
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaTypeItem
    *                        @type          : com.coretex.items.core.MetaTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String SUBTYPES = "subtypes";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : itemAttributes
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaTypeItem
    *                        @type          : com.coretex.items.core.MetaAttributeTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String ITEM_ATTRIBUTES = "itemAttributes";

    public MetaTypeItem() {
        super();
    }

    public MetaTypeItem(ItemContext ctx) {
        super(ctx);
    }
    /**
    * Item code
    */
    public String getTypeCode(){
        return getItemContext().getValue(TYPE_CODE);
    }

    /**
    * Item code
    */
    public void setTypeCode(String typeCode){
        getItemContext().setValue(TYPE_CODE, typeCode);
    }

    /**
    * Item Class Name
    */
    public Class getItemClass(){
        return getItemContext().getValue(ITEM_CLASS);
    }

    /**
    * Item Class Name
    */
    public void setItemClass(Class itemClass){
        getItemContext().setValue(ITEM_CLASS, itemClass);
    }

    /**
    * Table Name
    */
    public String getTableName(){
        return getItemContext().getValue(TABLE_NAME);
    }

    /**
    * Table Name
    */
    public void setTableName(String tableName){
        getItemContext().setValue(TABLE_NAME, tableName);
    }

    /**
    * Specify creation separate table for that item
    */
    public Boolean getTableOwner(){
        return getItemContext().getValue(TABLE_OWNER);
    }

    /**
    * Specify creation separate table for that item
    */
    public void setTableOwner(Boolean tableOwner){
        getItemContext().setValue(TABLE_OWNER, tableOwner);
    }

    /**
    * Description of type
    */
    public String getDescription(){
        return getItemContext().getValue(DESCRIPTION);
    }

    /**
    * Description of type
    */
    public void setDescription(String description){
        getItemContext().setValue(DESCRIPTION, description);
    }

    /**
    * Is abstract type
    */
    public Boolean getAbstract(){
        return getItemContext().getValue(ABSTRACT);
    }

    /**
    * Is abstract type
    */
    public void setAbstract(Boolean _abstract){
        getItemContext().setValue(ABSTRACT, _abstract);
    }

    /**
    * Super Type uuid
    */
    public MetaTypeItem getParent(){
        return getItemContext().getValue(PARENT);
    }

    /**
    * Super Type uuid
    */
    public void setParent(MetaTypeItem parent){
        getItemContext().setValue(PARENT, parent);
    }

    /**
    * Set of child types of this type
    */
    public Set<MetaTypeItem> getSubtypes(){
        return getItemContext().getValue(SUBTYPES);
    }

    /**
    * Set of child types of this type
    */
    public void setSubtypes(Set<MetaTypeItem> subtypes){
        getItemContext().setValue(SUBTYPES, subtypes);
    }

    /**
    * Super Type uuid
    */
    public Set<MetaAttributeTypeItem> getItemAttributes(){
        return getItemContext().getValue(ITEM_ATTRIBUTES);
    }

    /**
    * Super Type uuid
    */
    public void setItemAttributes(Set<MetaAttributeTypeItem> itemAttributes){
        getItemContext().setValue(ITEM_ATTRIBUTES, itemAttributes);
    }

}






