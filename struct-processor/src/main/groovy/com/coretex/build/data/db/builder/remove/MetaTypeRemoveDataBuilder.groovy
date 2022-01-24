package com.coretex.build.data.db.builder.remove

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.ClassItemDiffDataHolder
import com.coretex.build.data.db.removes.MetaTypeRemove
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = META_TYPE)
class MetaTypeRemoveDataBuilder implements RemoveDataBuilder<MetaTypeRemove, AbstractClassItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private AbstractItem essentialItem = items.find { it.code == META_TYPE.toString() }

    @Override
    List<MetaTypeRemove> build(AbstractClassItemDiffDataHolder dataHolder) {
        List<MetaTypeRemove> inserts = []
            inserts.add(genRemove(essentialItem, dataHolder))
        return inserts
    }

    static MetaTypeRemove genRemove(AbstractItem essentialItem, AbstractClassItemDiffDataHolder dataHolder) {
        MetaTypeRemove remove = new MetaTypeRemove()
        remove.essentialItem = essentialItem
        remove.data = dataHolder
        return remove
    }
}
