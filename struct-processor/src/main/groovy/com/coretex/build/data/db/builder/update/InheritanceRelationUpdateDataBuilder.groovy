package com.coretex.build.data.db.builder.update

import com.coretex.build.data.db.builder.inserts.InheritanceRelationInsertDataBuilder
import com.coretex.build.data.db.builder.remove.InheritanceRelationRemoveDataBuilder
import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.updates.InheritanceRelationUpdate
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.INHERITANCE_RELATION

@EssentialDataItem(itemCode = INHERITANCE_RELATION)
class InheritanceRelationUpdateDataBuilder implements UpdateDataBuilder<InheritanceRelationUpdate, AbstractClassItemDiffDataHolder> {

    @Override
    List<InheritanceRelationUpdate> build(AbstractClassItemDiffDataHolder dataHolder) {
        List<InheritanceRelationUpdate> updates = []

        if(dataHolder.parentChanged()){
            updates.add(new InheritanceRelationUpdate(delegates: new InheritanceRelationRemoveDataBuilder().build(dataHolder)))
            updates.add(new InheritanceRelationUpdate(delegates: new InheritanceRelationInsertDataBuilder().build(dataHolder)))
        }
        return updates
    }

}
