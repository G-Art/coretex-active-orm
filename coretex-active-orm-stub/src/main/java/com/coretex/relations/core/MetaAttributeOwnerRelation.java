
package com.coretex.relations.core;

/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 2021-12-20 18:56:30
* ----------------------------------------------------------------
*/

import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;

import java.time.LocalDateTime;

public class MetaAttributeOwnerRelation extends GenericItem {

    public static final String ITEM_TYPE = "MetaAttributeOwnerRelation";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : createDate
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeOwnerRelation
    *                        @type          : LocalDateTime
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String CREATE_DATE = "createDate";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : updateDate
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeOwnerRelation
    *                        @type          : LocalDateTime
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String UPDATE_DATE = "updateDate";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : metaType
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeOwnerRelation
    *                        @type          : com.coretex.items.core.MetaTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String META_TYPE = "metaType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : sourceType
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeOwnerRelation
    *                        @type          : com.coretex.items.core.MetaTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String SOURCE_TYPE = "sourceType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : targetType
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeOwnerRelation
    *                        @type          : com.coretex.items.core.MetaTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String TARGET_TYPE = "targetType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : source
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeOwnerRelation
    *                        @type          : com.coretex.items.core.MetaAttributeTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String SOURCE = "source";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : target
    *                        @optional      : false
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaAttributeOwnerRelation
    *                        @type          : com.coretex.items.core.MetaTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String TARGET = "target";

    public MetaAttributeOwnerRelation() {
        super();
    }

    public MetaAttributeOwnerRelation(ItemContext ctx) {
        super(ctx);
    }

/**
* Creation date
*/
public LocalDateTime getCreateDate(){
    return getItemContext().getValue(CREATE_DATE);
}

/**
* Creation date
*/
public void setCreateDate(LocalDateTime createDate){
    getItemContext().setValue(CREATE_DATE, createDate);
}
/**
* Updating date
*/
public LocalDateTime getUpdateDate(){
    return getItemContext().getValue(UPDATE_DATE);
}

/**
* Updating date
*/
public void setUpdateDate(LocalDateTime updateDate){
    getItemContext().setValue(UPDATE_DATE, updateDate);
}
/**
* Relation to meta type description
*/
public MetaTypeItem getMetaType(){
    return getItemContext().getValue(META_TYPE);
}

/**
* Relation to meta type description
*/
public void setMetaType(MetaTypeItem metaType){
    getItemContext().setValue(META_TYPE, metaType);
}
/**
* Specify attribute of source relation owner
*/
public MetaTypeItem getSourceType(){
    return getItemContext().getValue(SOURCE_TYPE);
}

/**
* Specify attribute of source relation owner
*/
public void setSourceType(MetaTypeItem sourceType){
    getItemContext().setValue(SOURCE_TYPE, sourceType);
}
/**
* Specify attribute of target relation owner
*/
public MetaTypeItem getTargetType(){
    return getItemContext().getValue(TARGET_TYPE);
}

/**
* Specify attribute of target relation owner
*/
public void setTargetType(MetaTypeItem targetType){
    getItemContext().setValue(TARGET_TYPE, targetType);
}
/**
* Specify source insistence
*/
public MetaAttributeTypeItem getSource(){
    return getItemContext().getValue(SOURCE);
}

/**
* Specify source insistence
*/
public void setSource(MetaAttributeTypeItem source){
    getItemContext().setValue(SOURCE, source);
}
/**
* Specify target insistence
*/
public MetaTypeItem getTarget(){
    return getItemContext().getValue(TARGET);
}

/**
* Specify target insistence
*/
public void setTarget(MetaTypeItem target){
    getItemContext().setValue(TARGET, target);
}
}







