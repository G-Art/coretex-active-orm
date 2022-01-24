package com.coretex.build.data.db.diff.dataholders

import com.coretex.build.data.items.Item
import com.coretex.build.data.items.attributes.Attribute

class AttributeItemDiffDataHolder extends ItemDiffDataHolder<Attribute<Item>> {

    List<String> indexes = []

    public static final String C_COLUMNNAME = "c_columnname"
    public static final String OWNER = "owner"

    String columnName(){
        return  item != null ? item.code == 'uuid' ? item.code : item.columnName : dbData.get(C_COLUMNNAME)
    }

    boolean ownerChanged(){
        def ip = item != null ? item.owner != null ? item.owner.uuid : null : null
        def dbIp = dbData != null ? dbData.get(OWNER) : null

        return ip != dbIp
    }



    @Override
    String toString() {
        return "AttributeItemDiffDataHolder{ uuid = ${itemUUID}, name = ${columnName()}";
    }
}
