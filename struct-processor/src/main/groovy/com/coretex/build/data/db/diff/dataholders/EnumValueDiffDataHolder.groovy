package com.coretex.build.data.db.diff.dataholders


import com.coretex.build.data.items.EnumValue

class EnumValueDiffDataHolder extends ItemDiffDataHolder<EnumValue> {


    @Override
    String toString() {
        return "EnumValueDiffDataHolder{uuid = ${itemUUID}}"
    }
}
