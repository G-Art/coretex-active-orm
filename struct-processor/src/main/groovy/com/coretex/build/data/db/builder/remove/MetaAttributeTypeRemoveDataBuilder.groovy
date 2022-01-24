package com.coretex.build.data.db.builder.remove

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.removes.MetaAttributeTypeRemove
import com.coretex.build.data.db.removes.MetaTypeRemove
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ATTRIBUTE_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = META_ATTRIBUTE_TYPE)
class MetaAttributeTypeRemoveDataBuilder implements RemoveDataBuilder<MetaTypeRemove, AttributeItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private AbstractItem essentialItem = items.find { it.code == META_ATTRIBUTE_TYPE.toString() }

    @Override
    List<MetaAttributeTypeRemove> build(AttributeItemDiffDataHolder dataHolder) {
        List<MetaAttributeTypeRemove> removes = []
        removes.add(genRemove(essentialItem, dataHolder))
        return removes
    }

    static MetaAttributeTypeRemove genRemove(AbstractItem essentialItem, AttributeItemDiffDataHolder dataHolder) {
        MetaAttributeTypeRemove remove = new MetaAttributeTypeRemove()
        remove.essentialItem = essentialItem
        remove.data = dataHolder
        return remove
    }
}
