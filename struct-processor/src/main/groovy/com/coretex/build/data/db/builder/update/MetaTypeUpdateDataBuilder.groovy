package com.coretex.build.data.db.builder.update

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.updates.MetaTypeUpdate
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = META_TYPE)
class MetaTypeUpdateDataBuilder implements UpdateDataBuilder<MetaTypeUpdate, AbstractClassItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private AbstractItem essentialItem = items.find { it.code == META_TYPE.toString() }

    @Override
    List<MetaTypeUpdate> build(AbstractClassItemDiffDataHolder dataHolder) {
        List<MetaTypeUpdate> updates = []
            updates.add(genUpdate(essentialItem, dataHolder))
        return updates
    }

    static MetaTypeUpdate genUpdate(AbstractItem essentialItem, AbstractClassItemDiffDataHolder dataHolder) {
        MetaTypeUpdate update = new MetaTypeUpdate()
        update.essentialItem = essentialItem
        update.data = dataHolder
        return update
    }
}
