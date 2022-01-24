package com.coretex.build.data.db.inserts.columns

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.inserts.RowItem
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.EnumItem
import com.coretex.build.data.items.EnumValue
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.RegularClassItem
import com.coretex.build.data.items.RelationItem
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.build.data.items.traits.Insertable
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ATTRIBUTE_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_VALUE_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_RELATION_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */

@EssentialDataItemColumn(columnName = 'metaType')
class MetaTypeColumn extends Column<RowItem<? extends Item>> {

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
    Object getRawValue() {
        Insertable type = null

        //TODO: move it in separate util method
        if (data.item.class in RegularClassItem) {
            type = regularMetaType
        } else if (data.item.class in EnumItem) {
            type = metaEnumType
        } else if (data.item.class in EnumValue) {
            type = metaEnumValueType
        } else if (data.item.class in RelationItem) {
            if(essentialItem.code == data.item.code){
                type = data.item
            }else {
                type = data.item.metaTypeClass
            }
        } else if (data.item.class in ClassItem) {
            type = metaType
        } else if (data.item.class in Attribute) {
            type = attributeMetaType
        }
        return type.uuid
    }

    @Override
    Object getValue() {
       def v = getRawValue()
       return Objects.isNull(v) ? nullValue : "'${v}'"
    }
}
