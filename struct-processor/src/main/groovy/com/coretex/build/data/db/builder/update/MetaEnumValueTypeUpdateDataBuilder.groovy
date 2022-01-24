package com.coretex.build.data.db.builder.update

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.EnumValueDiffDataHolder
import com.coretex.build.data.db.updates.MetaEnumTypeUpdate
import com.coretex.build.data.db.updates.MetaEnumValueTypeUpdate
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_VALUE_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = META_ENUM_VALUE_TYPE)
class MetaEnumValueTypeUpdateDataBuilder implements UpdateDataBuilder<MetaEnumValueTypeUpdate, EnumValueDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private ClassItem essentialItem = items.find { it.code == META_ENUM_VALUE_TYPE.toString() } as ClassItem


    @Override
    List<MetaEnumValueTypeUpdate> build(EnumValueDiffDataHolder dataHolder) {
        List<MetaEnumValueTypeUpdate> updates = []
        updates.add(genUpdate(essentialItem, dataHolder))
        return updates
    }

    private static MetaEnumValueTypeUpdate genUpdate(ClassItem essentialItem, EnumValueDiffDataHolder dataHolder) {
        MetaEnumValueTypeUpdate update = new MetaEnumValueTypeUpdate()
        update.essentialItem = essentialItem
        update.data = dataHolder
        return update
    }
}
