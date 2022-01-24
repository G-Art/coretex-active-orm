package com.coretex.build.data.db.updates.columns

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.updates.Update
import com.coretex.build.data.items.*
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.build.data.items.traits.Insertable
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */

@EssentialDataItemColumn(columnName = 'metaType')
class MetaTypeColumn extends Column<ItemDiffDataHolder, Update> {

    private ClassItem metaType

    private ClassItem metaRelationType

    private ClassItem attributeMetaType

    private ClassItem regularMetaType

    private ClassItem metaEnumType

    private ClassItem metaEnumValueType

    MetaTypeColumn() {
        List<AbstractItem> items = CoretexPluginContext.instance.items
        metaType = items.find { it.code == META_TYPE.toString() } as ClassItem
        metaRelationType = items.find { it.code == META_RELATION_TYPE.toString() } as ClassItem
        regularMetaType = items.find { it.code == REGULAR_TYPE.toString() } as ClassItem
        attributeMetaType = items.find { it.code == META_ATTRIBUTE_TYPE.toString() } as ClassItem
        metaEnumType = items.find { it.code == META_ENUM_TYPE.toString() } as ClassItem
        metaEnumValueType = items.find { it.code == META_ENUM_VALUE_TYPE.toString() } as ClassItem
    }

    @Override
    Object getValue() {

            Insertable type = null
            def item = data.item
            //TODO: move it in separate util method
            if (item.class in RegularClassItem) {
                type = regularMetaType
            } else if (item.class in EnumItem) {
                type = metaEnumType
            } else if (item.class in EnumValue) {
                type = metaEnumValueType
            } else if (item.class in RelationItem) {
                if(essentialItem.code == item.code){
                    type = item
                }else {
                    type = item.metaTypeClass
                }
            } else if (item.class in ClassItem) {
                type = metaType
            } else if (item.class in Attribute) {
                type = attributeMetaType
            }
            return type?.uuid ? "'${type.uuid}'" : nullValue

    }
}
