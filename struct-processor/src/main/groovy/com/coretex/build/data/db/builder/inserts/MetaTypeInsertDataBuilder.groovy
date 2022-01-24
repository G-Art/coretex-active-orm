package com.coretex.build.data.db.builder.inserts

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.inserts.MetaTypeInsert
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RelationItem
import com.coretex.build.data.items.traits.DbEntity
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = META_TYPE)
class MetaTypeInsertDataBuilder implements InsertDataBuilder<MetaTypeInsert, AbstractClassItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private ClassItem essentialItem = items.find { it.code == META_TYPE.toString() } as ClassItem

    @Override
    List<MetaTypeInsert> build() {
        println "Generate essential data for ${essentialItem?.code}"
        List<MetaTypeInsert> inserts = []
        items.findAll { item -> item.class == ClassItem || item.class == RelationItem }.each {
            inserts.add(genInsert(essentialItem, it))
        }
        return inserts
    }

    @Override
    List<MetaTypeInsert> build(AbstractClassItemDiffDataHolder dataHolder) {
        List<MetaTypeInsert> inserts = []
        if(dataHolder.isClass() || dataHolder.isRelation()){
            inserts.add(genInsert(essentialItem, dataHolder.item as AbstractItem))
        }
        return inserts
    }

    static MetaTypeInsert genInsert(ClassItem essentialItem, AbstractItem item) {
        MetaTypeInsert insert = new MetaTypeInsert()
        insert.essentialItem = essentialItem
        insert.data = new MetaTypeInsert.MetaTypeRowItem(item)
        return insert
    }
}
