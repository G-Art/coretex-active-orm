
package com.coretex.items.core;

/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 2021-12-20 18:56:29
* ----------------------------------------------------------------
*/

import com.coretex.orm.core.services.items.context.ItemContext;


/**
 * Meta Type Attributes
 */
public class MetaAttributeTypeItem extends GenericItem {

    public static final String ITEM_TYPE = "MetaAttributeType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : attributeTypeCode
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String ATTRIBUTE_TYPE_CODE = "attributeTypeCode";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : attributeType
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : com.coretex.items.core.GenericItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String ATTRIBUTE_TYPE = "attributeType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : attributeName
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String ATTRIBUTE_NAME = "attributeName";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : localized
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : Boolean
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String LOCALIZED = "localized";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : associated
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  : false
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : Boolean
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String ASSOCIATED = "associated";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : columnName
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String COLUMN_NAME = "columnName";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : containerType
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : Class
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String CONTAINER_TYPE = "containerType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : description
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String DESCRIPTION = "description";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : unique
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  : false
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : Boolean
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String UNIQUE = "unique";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : optional
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  : true
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : Boolean
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String OPTIONAL = "optional";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : dynamic
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  : false
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : Boolean
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String DYNAMIC = "dynamic";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : defaultValue
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : String
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String DEFAULT_VALUE = "defaultValue";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : source
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : Boolean
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String SOURCE = "source";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : owner
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeTypeItem
    *                        @type          : com.coretex.items.core.MetaTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String OWNER = "owner";

    public MetaAttributeTypeItem() {
        super();
    }

    public MetaAttributeTypeItem(ItemContext ctx) {
        super(ctx);
    }
    /**
    * Describes the type of attribute
    */
    public String getAttributeTypeCode(){
        return getItemContext().getValue(ATTRIBUTE_TYPE_CODE);
    }

    /**
    * Describes the type of attribute
    */
    public void setAttributeTypeCode(String attributeTypeCode){
        getItemContext().setValue(ATTRIBUTE_TYPE_CODE, attributeTypeCode);
    }

    /**
    * The type of a value that attribute returns
    */
    public GenericItem getAttributeType(){
        return getItemContext().getValue(ATTRIBUTE_TYPE);
    }

    /**
    * The type of a value that attribute returns
    */
    public void setAttributeType(GenericItem attributeType){
        getItemContext().setValue(ATTRIBUTE_TYPE, attributeType);
    }

    /**
    * Attribute Name
    */
    public String getAttributeName(){
        return getItemContext().getValue(ATTRIBUTE_NAME);
    }

    /**
    * Attribute Name
    */
    public void setAttributeName(String attributeName){
        getItemContext().setValue(ATTRIBUTE_NAME, attributeName);
    }

    /**
    * It is localized field
    */
    public Boolean getLocalized(){
        return getItemContext().getValue(LOCALIZED);
    }

    /**
    * It is localized field
    */
    public void setLocalized(Boolean localized){
        getItemContext().setValue(LOCALIZED, localized);
    }

    /**
    * Associated item(s) 'false' by default
    */
    public Boolean getAssociated(){
        return getItemContext().getValue(ASSOCIATED);
    }

    /**
    * Associated item(s) 'false' by default
    */
    public void setAssociated(Boolean associated){
        getItemContext().setValue(ASSOCIATED, associated);
    }

    /**
    * Column Name
    */
    public String getColumnName(){
        return getItemContext().getValue(COLUMN_NAME);
    }

    /**
    * Column Name
    */
    public void setColumnName(String columnName){
        getItemContext().setValue(COLUMN_NAME, columnName);
    }

    /**
    * Describes the container type of attribute return value
    */
    public Class getContainerType(){
        return getItemContext().getValue(CONTAINER_TYPE);
    }

    /**
    * Describes the container type of attribute return value
    */
    public void setContainerType(Class containerType){
        getItemContext().setValue(CONTAINER_TYPE, containerType);
    }

    /**
    * Description of attribute
    */
    public String getDescription(){
        return getItemContext().getValue(DESCRIPTION);
    }

    /**
    * Description of attribute
    */
    public void setDescription(String description){
        getItemContext().setValue(DESCRIPTION, description);
    }

    /**
    * Is Attributes should to be unique
    */
    public Boolean getUnique(){
        return getItemContext().getValue(UNIQUE);
    }

    /**
    * Is Attributes should to be unique
    */
    public void setUnique(Boolean unique){
        getItemContext().setValue(UNIQUE, unique);
    }

    /**
    * Is Attributes should to be optional
    */
    public Boolean getOptional(){
        return getItemContext().getValue(OPTIONAL);
    }

    /**
    * Is Attributes should to be optional
    */
    public void setOptional(Boolean optional){
        getItemContext().setValue(OPTIONAL, optional);
    }

    /**
    * Is Attribute should be stored in persistence provider
    */
    public Boolean getDynamic(){
        return getItemContext().getValue(DYNAMIC);
    }

    /**
    * Is Attribute should be stored in persistence provider
    */
    public void setDynamic(Boolean dynamic){
        getItemContext().setValue(DYNAMIC, dynamic);
    }

    /**
    * default value expression statement
    */
    public String getDefaultValue(){
        return getItemContext().getValue(DEFAULT_VALUE);
    }

    /**
    * default value expression statement
    */
    public void setDefaultValue(String defaultValue){
        getItemContext().setValue(DEFAULT_VALUE, defaultValue);
    }

    /**
    * Specify that this attribute is source in relation
    */
    public Boolean getSource(){
        return getItemContext().getValue(SOURCE);
    }

    /**
    * Specify that this attribute is source in relation
    */
    public void setSource(Boolean source){
        getItemContext().setValue(SOURCE, source);
    }

    /**
    * Owner of this attribute
    */
    public MetaTypeItem getOwner(){
        return getItemContext().getValue(OWNER);
    }

    /**
    * Owner of this attribute
    */
    public void setOwner(MetaTypeItem owner){
        getItemContext().setValue(OWNER, owner);
    }

}






