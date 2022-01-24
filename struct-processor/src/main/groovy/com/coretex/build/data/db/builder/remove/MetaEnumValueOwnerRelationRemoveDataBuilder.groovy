package com.coretex.build.data.db.builder.remove

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.EnumValueDiffDataHolder
import com.coretex.build.data.db.removes.MetaEnumValueOwnerRelationRemove
import com.coretex.build.data.db.removes.MetaEnumValueTypeRemove
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.ENUM_VALUE_OWNER_RELATION
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_VALUE_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = ENUM_VALUE_OWNER_RELATION)
class MetaEnumValueOwnerRelationRemoveDataBuilder implements RemoveDataBuilder<MetaEnumValueOwnerRelationRemove, EnumValueDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private AbstractItem essentialItem = items.find { it.code == ENUM_VALUE_OWNER_RELATION.toString() }

    @Override
    List<MetaEnumValueOwnerRelationRemove> build(EnumValueDiffDataHolder dataHolder) {
        List<MetaEnumValueOwnerRelationRemove> actions = []
        actions.add(genRemove(essentialItem, dataHolder))
        return actions
    }

    static MetaEnumValueOwnerRelationRemove genRemove(AbstractItem essentialItem, EnumValueDiffDataHolder dataHolder) {
        MetaEnumValueOwnerRelationRemove remove = new MetaEnumValueOwnerRelationRemove()
        remove.essentialItem = essentialItem
        remove.data = dataHolder
        return remove
    }
}
