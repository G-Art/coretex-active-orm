package com.coretex.build.data.db.diff.dataholders

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.EnumItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.RegularClassItem
import com.coretex.build.data.items.RelationItem

abstract class ItemDiffDataHolder<I extends Item> {
    I item
    Map<String, Object> dbData = [:]

    I getItem() {
        return item
    }

    UUID getItemUUID(){
        return item != null ? item.uuid : dbData.get("uuid") as UUID
    }

    Class<?> getItemClass(){
        return item.class
    }

    boolean isItemType(Class<? extends Item> aClass){
        return getItemClass() in aClass
    }

    boolean isRemoved(){
        return Objects.isNull(item)
    }

    boolean isNew(){
        return dbData.isEmpty()
    }

    boolean isRelation(){
        return isItemType(RelationItem)
    }
    boolean isClass(){
        return isItemType(ClassItem)
    }
    boolean isEnum(){
        return isItemType(EnumItem)
    }
    boolean isRegular(){
        return isItemType(RegularClassItem)
    }

}
