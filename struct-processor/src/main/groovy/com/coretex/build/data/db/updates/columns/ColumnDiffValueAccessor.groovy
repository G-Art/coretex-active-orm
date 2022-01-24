package com.coretex.build.data.db.updates.columns

import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.RelationItem
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.Constants.COLUMN_PREFIX
import static com.coretex.common.utils.BuildUtils.collectAllAttributesForItem

trait ColumnDiffValueAccessor<I extends ItemDiffDataHolder<? extends Item>> {

    I data

    String getColumnName() {
        EssentialDataItemColumn annotation = this.class.getAnnotation(EssentialDataItemColumn)
        if (essentialItem.class in RelationItem) {
            if ('source' == annotation.columnName() || 'target' == annotation.columnName()) {
                return COLUMN_PREFIX + annotation.columnName()
            }
            Attribute attribute = collectAllAttributesForItem((essentialItem as RelationItem).metaTypeClass).find {
                it.name == annotation.columnName()
            }
            if(attribute == null){
                return COLUMN_PREFIX + annotation.columnName()
            }
            return attribute.columnName
        }
        if (essentialItem.class in ClassItem) {
            Attribute attribute = collectAllAttributesForItem(essentialItem as ClassItem).find {
                it.name == annotation.columnName()
            }
            if(attribute == null){
                return COLUMN_PREFIX + annotation.columnName()
            }
            return attribute.columnName
        }
        return COLUMN_PREFIX + annotation.columnName()

    }

    abstract AbstractItem getEssentialItem()
}