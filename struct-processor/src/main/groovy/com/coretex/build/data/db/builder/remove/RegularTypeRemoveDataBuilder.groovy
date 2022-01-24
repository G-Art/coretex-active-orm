package com.coretex.build.data.db.builder.remove

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.EnumItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.RegularClassItemDiffDataHolder
import com.coretex.build.data.db.removes.MetaEnumTypeRemove
import com.coretex.build.data.db.removes.RegularClassTypeRemove
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = REGULAR_TYPE)
class RegularTypeRemoveDataBuilder implements RemoveDataBuilder<RegularClassTypeRemove, RegularClassItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private AbstractItem essentialItem = items.find { it.code == REGULAR_TYPE.toString() }

    @Override
    List<RegularClassTypeRemove> build(RegularClassItemDiffDataHolder dataHolder) {
        List<RegularClassTypeRemove> inserts = []
            inserts.add(genRemove(essentialItem, dataHolder))
        return inserts
    }

    static RegularClassTypeRemove genRemove(AbstractItem essentialItem, RegularClassItemDiffDataHolder dataHolder) {
        RegularClassTypeRemove insert = new RegularClassTypeRemove()
        insert.essentialItem = essentialItem
        insert.data = dataHolder
        return insert
    }
}
