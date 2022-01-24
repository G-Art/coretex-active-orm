package com.coretex.build.data.db.builder.remove

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.removes.AttributeOwnerRelationRemove
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.ATTRIBUTE_OWNER_RELATION

@EssentialDataItem(itemCode = ATTRIBUTE_OWNER_RELATION)
class AttributeOwnerRelationRemoveDataBuilder implements RemoveDataBuilder<AttributeOwnerRelationRemove, AttributeItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private AbstractItem essentialItem = items.find { it.code == ATTRIBUTE_OWNER_RELATION.toString() }

    @Override
    List<AttributeOwnerRelationRemove> build(AttributeItemDiffDataHolder dataHolder) {
        List<AttributeOwnerRelationRemove> removes = []
        removes.add(genRemove(essentialItem, dataHolder))
        return removes
    }

    static AttributeOwnerRelationRemove genRemove(AbstractItem essentialItem, AttributeItemDiffDataHolder dataHolder) {
        AttributeOwnerRelationRemove remove = new AttributeOwnerRelationRemove()
        remove.essentialItem = essentialItem
        remove.data = dataHolder
        return remove
    }

}
