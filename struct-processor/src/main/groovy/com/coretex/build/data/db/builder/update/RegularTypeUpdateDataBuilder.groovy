package com.coretex.build.data.db.builder.update

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.RegularClassItemDiffDataHolder
import com.coretex.build.data.db.updates.RegularClassTypeUpdate
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = REGULAR_TYPE)
class RegularTypeUpdateDataBuilder implements UpdateDataBuilder<RegularClassTypeUpdate, RegularClassItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private AbstractItem essentialItem = items.find { it.code == REGULAR_TYPE.toString() }

    @Override
    List<RegularClassTypeUpdate> build(RegularClassItemDiffDataHolder dataHolder) {
        List<RegularClassTypeUpdate> inserts = []
            inserts.add(genUpdate(essentialItem, dataHolder))
        return inserts
    }

    static RegularClassTypeUpdate genUpdate(AbstractItem essentialItem, RegularClassItemDiffDataHolder dataHolder) {
        RegularClassTypeUpdate update = new RegularClassTypeUpdate()
        update.essentialItem = essentialItem
        update.data = dataHolder
        return update
    }
}
