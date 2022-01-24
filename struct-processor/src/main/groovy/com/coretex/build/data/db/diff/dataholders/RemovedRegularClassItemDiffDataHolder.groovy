package com.coretex.build.data.db.diff.dataholders


import com.coretex.build.data.items.RegularClassItem

class RemovedRegularClassItemDiffDataHolder extends RegularClassItemDiffDataHolder {

    @Override
    Class<?> getItemClass() {
        return RegularClassItem.class
    }
}
