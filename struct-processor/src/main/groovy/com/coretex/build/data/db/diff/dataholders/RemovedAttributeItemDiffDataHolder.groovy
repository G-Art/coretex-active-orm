package com.coretex.build.data.db.diff.dataholders

import com.coretex.build.data.items.attributes.Attribute

class RemovedAttributeItemDiffDataHolder extends AttributeItemDiffDataHolder {

    @Override
    Class<?> getItemClass() {
        return Attribute.class
    }
}
