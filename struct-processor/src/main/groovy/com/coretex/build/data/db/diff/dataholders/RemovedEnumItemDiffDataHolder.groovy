package com.coretex.build.data.db.diff.dataholders

import com.coretex.build.data.items.EnumItem
import com.coretex.build.data.items.EnumValue

class RemovedEnumItemDiffDataHolder extends EnumItemDiffDataHolder{

    @Override
    Class<?> getItemClass() {
        return EnumItem.class
    }
}
