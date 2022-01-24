package com.coretex.build.data.db.builder.update

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.builder.inserts.AttributeOwnerRelationInsertDataBuilder
import com.coretex.build.data.db.builder.inserts.MetaEnumValueOwnerRelationDataBuilder
import com.coretex.build.data.db.builder.remove.AttributeOwnerRelationRemoveDataBuilder
import com.coretex.build.data.db.builder.remove.MetaEnumValueOwnerRelationRemoveDataBuilder
import com.coretex.build.data.db.builder.remove.RemoveDataBuilder
import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.EnumValueDiffDataHolder
import com.coretex.build.data.db.removes.MetaEnumValueOwnerRelationRemove
import com.coretex.build.data.db.updates.AttributeOwnerRelationUpdate
import com.coretex.build.data.db.updates.MetaEnumValueOwnerRelationUpdate
import com.coretex.build.data.items.AbstractItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.ENUM_VALUE_OWNER_RELATION

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = ENUM_VALUE_OWNER_RELATION)
class MetaEnumValueOwnerRelationUpdateDataBuilder implements UpdateDataBuilder<MetaEnumValueOwnerRelationUpdate, EnumValueDiffDataHolder> {

    @Override
    List<MetaEnumValueOwnerRelationUpdate> build(EnumValueDiffDataHolder dataHolder) {
        List<MetaEnumValueOwnerRelationUpdate> updates = []
        if(dataHolder.isRemoved()){
            updates.add(new MetaEnumValueOwnerRelationUpdate(delegates: new MetaEnumValueOwnerRelationRemoveDataBuilder().build(dataHolder)))
        }
        if(dataHolder.isNew()){
            updates.add(new MetaEnumValueOwnerRelationUpdate(delegates: new MetaEnumValueOwnerRelationDataBuilder().build(dataHolder)))
        }
        return updates
    }

}
