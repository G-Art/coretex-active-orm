package com.coretex.build.data.db.diff.dataholders

import com.coretex.build.data.items.ClassItem

class RemovedClassItemDiffDataHolder extends ClassItemDiffDataHolder {

    @Override
    Class<?> getItemClass() {
        return ClassItem.class
    }
}
