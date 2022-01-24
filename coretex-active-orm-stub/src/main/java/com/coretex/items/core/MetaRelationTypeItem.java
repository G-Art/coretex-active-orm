
package com.coretex.items.core;

/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 2021-12-20 18:56:30
* ----------------------------------------------------------------
*/

import com.coretex.orm.core.services.items.context.ItemContext;


/**
 * Relation type holds information of relations between items
 */
public class MetaRelationTypeItem extends MetaTypeItem {

    public static final String ITEM_TYPE = "MetaRelationType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : sourceType
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaRelationTypeItem
    *                        @type          : com.coretex.items.core.MetaTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String SOURCE_TYPE = "sourceType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : sourceAttribute
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaRelationTypeItem
    *                        @type          : com.coretex.items.core.MetaAttributeTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String SOURCE_ATTRIBUTE = "sourceAttribute";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : targetType
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaRelationTypeItem
    *                        @type          : com.coretex.items.core.MetaTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String TARGET_TYPE = "targetType";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : targetAttribute
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : MetaRelationTypeItem
    *                        @type          : com.coretex.items.core.MetaAttributeTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String TARGET_ATTRIBUTE = "targetAttribute";

    public MetaRelationTypeItem() {
        super();
    }

    public MetaRelationTypeItem(ItemContext ctx) {
        super(ctx);
    }
    /**
    * Specify type of source insistence
    */
    public MetaTypeItem getSourceType(){
        return getItemContext().getValue(SOURCE_TYPE);
    }

    /**
    * Specify type of source insistence
    */
    public void setSourceType(MetaTypeItem sourceType){
        getItemContext().setValue(SOURCE_TYPE, sourceType);
    }

    /**
    * Specify attribute of source relation owner
    */
    public MetaAttributeTypeItem getSourceAttribute(){
        return getItemContext().getValue(SOURCE_ATTRIBUTE);
    }

    /**
    * Specify attribute of source relation owner
    */
    public void setSourceAttribute(MetaAttributeTypeItem sourceAttribute){
        getItemContext().setValue(SOURCE_ATTRIBUTE, sourceAttribute);
    }

    /**
    * Specify type of target insistence
    */
    public MetaTypeItem getTargetType(){
        return getItemContext().getValue(TARGET_TYPE);
    }

    /**
    * Specify type of target insistence
    */
    public void setTargetType(MetaTypeItem targetType){
        getItemContext().setValue(TARGET_TYPE, targetType);
    }

    /**
    * Specify attribute of target relation owner
    */
    public MetaAttributeTypeItem getTargetAttribute(){
        return getItemContext().getValue(TARGET_ATTRIBUTE);
    }

    /**
    * Specify attribute of target relation owner
    */
    public void setTargetAttribute(MetaAttributeTypeItem targetAttribute){
        getItemContext().setValue(TARGET_ATTRIBUTE, targetAttribute);
    }

}






