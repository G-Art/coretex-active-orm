package com.coretex.build.data.db.builder.remove

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.EnumItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.EnumValueDiffDataHolder
import com.coretex.build.data.db.removes.MetaEnumTypeRemove
import com.coretex.build.data.db.removes.MetaEnumValueTypeRemove
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_VALUE_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = META_ENUM_VALUE_TYPE)
class MetaEnumValueTypeRemoveDataBuilder implements RemoveDataBuilder<MetaEnumValueTypeRemove, EnumValueDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private AbstractItem essentialItem = items.find { it.code == META_ENUM_VALUE_TYPE.toString() }

    @Override
    List<MetaEnumValueTypeRemove> build(EnumValueDiffDataHolder dataHolder) {
        List<MetaEnumValueTypeRemove> actions = []
        actions.add(genRemove(essentialItem, dataHolder))
        return actions
    }

    static MetaEnumValueTypeRemove genRemove(AbstractItem essentialItem, EnumValueDiffDataHolder dataHolder) {
        MetaEnumValueTypeRemove remove = new MetaEnumValueTypeRemove()
        remove.essentialItem = essentialItem
        remove.data = dataHolder
        return remove
    }
}
