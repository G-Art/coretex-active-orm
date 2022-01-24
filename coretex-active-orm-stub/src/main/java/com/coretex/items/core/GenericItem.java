
package com.coretex.items.core;

/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 2021-12-20 18:56:29
* ----------------------------------------------------------------
*/

import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.meta.AbstractGenericItem;

import java.time.LocalDateTime;

/**
 * Generic item
 */
public abstract class GenericItem extends AbstractGenericItem {

    public static final String ITEM_TYPE = "Generic";

    /**
    * ------------------------------------------------------------------------------------------------------------------
    *
    * Generated field info { @name          : createDate
    *                        @optional      : true
    *                        @unique        : false
    *                        @defaultValue  :  n/a 
    *                        @localized     : false
    *                        @associated    : false
    *                        @owner         : GenericItem
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
    *                        @owner         : GenericItem
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
    *                        @owner         : GenericItem
    *                        @type          : com.coretex.items.core.MetaTypeItem
    *
    * ------------------------------------------------------------------------------------------------------------------
    */
    public static final String META_TYPE = "metaType";

    public GenericItem() {
        super();
    }

    public GenericItem(ItemContext ctx) {
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

}






