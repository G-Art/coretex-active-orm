package com.coretex.build.data.db.diff.dataholders

import com.coretex.build.data.items.RelationItem

class RemovedRelationItemDiffDataHolder extends RelationItemDiffDataHolder {

    @Override
    Class<?> getItemClass() {
        return RelationItem.class
    }
}
