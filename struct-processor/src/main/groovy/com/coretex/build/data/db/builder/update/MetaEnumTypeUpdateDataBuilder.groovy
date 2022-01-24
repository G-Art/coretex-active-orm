package com.coretex.build.data.db.builder.update

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.builder.inserts.InsertDataBuilder
import com.coretex.build.data.db.diff.dataholders.EnumItemDiffDataHolder
import com.coretex.build.data.db.inserts.MetaEnumTypeInsert
import com.coretex.build.data.db.updates.MetaEnumTypeUpdate
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.EnumItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = META_ENUM_TYPE)
class MetaEnumTypeUpdateDataBuilder implements UpdateDataBuilder<MetaEnumTypeUpdate, EnumItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private ClassItem essentialItem = items.find { it.code == META_ENUM_TYPE.toString() } as ClassItem


    @Override
    List<MetaEnumTypeUpdate> build(EnumItemDiffDataHolder dataHolder) {
        List<MetaEnumTypeUpdate> updates = []
        if (dataHolder.isEnum()) {
            updates.add(genUpdate(essentialItem, dataHolder))
        }
        return updates
    }

    private static MetaEnumTypeUpdate genUpdate(ClassItem essentialItem, EnumItemDiffDataHolder dataHolder) {
        MetaEnumTypeUpdate update = new MetaEnumTypeUpdate()
        update.essentialItem = essentialItem
        update.data = dataHolder
        return update
    }
}
