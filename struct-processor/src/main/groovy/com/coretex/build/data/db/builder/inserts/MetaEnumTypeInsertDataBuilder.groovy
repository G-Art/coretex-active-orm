package com.coretex.build.data.db.builder.inserts

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.EnumItemDiffDataHolder
import com.coretex.build.data.db.inserts.MetaEnumTypeInsert
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.EnumItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_ENUM_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = META_ENUM_TYPE)
class MetaEnumTypeInsertDataBuilder implements InsertDataBuilder<MetaEnumTypeInsert, EnumItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private ClassItem essentialItem = items.find { it.code == META_ENUM_TYPE.toString() } as ClassItem

    @Override
    List<MetaEnumTypeInsert> build() {
        println "Generate essential data for ${essentialItem.code}"
        List<MetaEnumTypeInsert> inserts = []
        items.findAll { item -> item.class in EnumItem }
                .each {
                    inserts.add(genInsert(essentialItem, it as EnumItem))
                }
        return inserts
    }

    @Override
    List<MetaEnumTypeInsert> build(EnumItemDiffDataHolder dataHolder) {
        List<MetaEnumTypeInsert> inserts = []
        if (dataHolder.isEnum()) {
            inserts.add(genInsert(essentialItem, dataHolder.item))
        }
        return inserts
    }

    private static MetaEnumTypeInsert genInsert(ClassItem essentialItem, EnumItem classItem) {
        MetaEnumTypeInsert insert = new MetaEnumTypeInsert()
        insert.essentialItem = essentialItem
        insert.data = new MetaEnumTypeInsert.EnumRowItem(classItem)
        return insert
    }
}
