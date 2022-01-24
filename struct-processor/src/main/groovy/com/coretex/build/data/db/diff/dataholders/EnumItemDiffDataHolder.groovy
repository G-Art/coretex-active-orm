package com.coretex.build.data.db.diff.dataholders

import com.coretex.build.data.items.EnumItem
import com.coretex.build.data.items.EnumValue

class EnumItemDiffDataHolder extends ItemDiffDataHolder<EnumItem> {

    Map<UUID, EnumValueDiffDataHolder> diffDataHolderMap = [:]


    @Override
    String toString() {
        return "EnumItemDiffDataHolder{uuid = ${itemUUID}}";
    }
}
