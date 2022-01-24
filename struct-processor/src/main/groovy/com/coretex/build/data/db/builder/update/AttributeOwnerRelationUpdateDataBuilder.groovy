package com.coretex.build.data.db.builder.update

import com.coretex.build.data.db.builder.inserts.AttributeOwnerRelationInsertDataBuilder
import com.coretex.build.data.db.builder.remove.AttributeOwnerRelationRemoveDataBuilder
import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.updates.AttributeOwnerRelationUpdate
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.ATTRIBUTE_OWNER_RELATION

@EssentialDataItem(itemCode = ATTRIBUTE_OWNER_RELATION)
class AttributeOwnerRelationUpdateDataBuilder implements UpdateDataBuilder<AttributeOwnerRelationUpdate, AttributeItemDiffDataHolder> {

    @Override
    List<AttributeOwnerRelationUpdate> build(AttributeItemDiffDataHolder dataHolder) {
        List<AttributeOwnerRelationUpdate> updates = []
        if(dataHolder.isRemoved() || dataHolder.ownerChanged()){
            updates.add(new AttributeOwnerRelationUpdate(delegates: new AttributeOwnerRelationRemoveDataBuilder().build(dataHolder)))
        }
        if(dataHolder.isNew() || dataHolder.ownerChanged()){
            updates.add(new AttributeOwnerRelationUpdate(delegates: new AttributeOwnerRelationInsertDataBuilder().build(dataHolder)))
        }
        return updates
    }

}
