package com.coretex.build.data.db.builder.remove

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.removes.InheritanceRelationRemove
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.INHERITANCE_RELATION

@EssentialDataItem(itemCode = INHERITANCE_RELATION)
class InheritanceRelationRemoveDataBuilder implements RemoveDataBuilder<InheritanceRelationRemove, AbstractClassItemDiffDataHolder> {


    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private AbstractItem essentialItem = items.find { it.code == INHERITANCE_RELATION.toString() }

    @Override
    List<InheritanceRelationRemove> build(AbstractClassItemDiffDataHolder dataHolder) {
        List<InheritanceRelationRemove> inserts = []
        inserts.add(genRemove(essentialItem, dataHolder))
        return inserts
    }

    static InheritanceRelationRemove genRemove(AbstractItem essentialItem, AbstractClassItemDiffDataHolder dataHolder) {
        InheritanceRelationRemove remove = new InheritanceRelationRemove()
        remove.essentialItem = essentialItem
        remove.data = dataHolder
        return remove
    }

}
