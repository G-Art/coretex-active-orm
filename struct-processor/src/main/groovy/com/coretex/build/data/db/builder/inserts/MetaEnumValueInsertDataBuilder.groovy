package com.coretex.build.data.db.builder.inserts

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.EnumValueDiffDataHolder
import com.coretex.build.data.db.inserts.MetaEnumValueTypeInsert
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.EnumItem
import com.coretex.build.data.items.EnumValue
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_VALUE_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = META_ENUM_VALUE_TYPE)
class MetaEnumValueInsertDataBuilder implements InsertDataBuilder<MetaEnumValueTypeInsert, EnumValueDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private ClassItem essentialItem = items.find { it.code == META_ENUM_VALUE_TYPE.toString() } as ClassItem

    @Override
    List<MetaEnumValueTypeInsert> build() {
        ClassItem essentialItem = items.find { it.code == META_ENUM_VALUE_TYPE.toString() } as ClassItem
        println "Generate essential data for ${essentialItem.code}"
        List<MetaEnumValueTypeInsert> inserts = []
        items.findAll { item -> item.class in EnumItem }
                .each {
                    (it as EnumItem).values.forEach {
                        enumValue -> inserts.add(genInsert(essentialItem, it as EnumItem, enumValue))
                    }
                }
        return inserts
    }

    @Override
    List<MetaEnumValueTypeInsert> build(EnumValueDiffDataHolder dataHolder) {
        List<MetaEnumValueTypeInsert> inserts = []
        if (dataHolder.itemClass in EnumValue) {
            inserts.add(genInsert(essentialItem, dataHolder.item.owner, dataHolder.item))
        }
        return inserts
    }

    private static MetaEnumValueTypeInsert genInsert(ClassItem essentialItem, EnumItem classItem, EnumValue enumValue) {
        MetaEnumValueTypeInsert insert = new MetaEnumValueTypeInsert()
        insert.essentialItem = essentialItem
        insert.data = new MetaEnumValueTypeInsert.ValueForEnumItem(enumValue, classItem)
        return insert
    }
}
