package com.coretex.build.data.db.builder.remove

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.ClassItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.EnumItemDiffDataHolder
import com.coretex.build.data.db.removes.MetaEnumTypeRemove
import com.coretex.build.data.db.removes.MetaTypeRemove
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = META_ENUM_TYPE)
class MetaEnumTypeRemoveDataBuilder implements RemoveDataBuilder<MetaEnumTypeRemove, EnumItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private AbstractItem essentialItem = items.find { it.code == META_ENUM_TYPE.toString() }

    @Override
    List<MetaEnumTypeRemove> build(EnumItemDiffDataHolder dataHolder) {
        List<MetaEnumTypeRemove> inserts = []
            inserts.add(genRemove(essentialItem, dataHolder))
        return inserts
    }

    static MetaEnumTypeRemove genRemove(AbstractItem essentialItem, EnumItemDiffDataHolder dataHolder) {
        MetaEnumTypeRemove insert = new MetaEnumTypeRemove()
        insert.essentialItem = essentialItem
        insert.data = dataHolder
        return insert
    }
}
