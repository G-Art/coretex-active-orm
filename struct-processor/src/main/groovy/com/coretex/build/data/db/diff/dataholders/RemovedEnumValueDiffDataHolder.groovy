package com.coretex.build.data.db.diff.dataholders

import com.coretex.build.data.items.EnumValue

class RemovedEnumValueDiffDataHolder extends EnumValueDiffDataHolder{
    @Override
    Class<?> getItemClass() {
        return EnumValue.class
    }
}
