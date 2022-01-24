package com.coretex.build.data.db.builder.inserts

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.RegularClassItemDiffDataHolder
import com.coretex.build.data.db.inserts.RegularTypeInsert
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RegularClassItem
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */

@EssentialDataItem(itemCode = REGULAR_TYPE)
class RegularTypeInsertDataBuilder implements InsertDataBuilder<RegularTypeInsert, RegularClassItemDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items
    private ClassItem essentialItem = items.find { it.code == REGULAR_TYPE.toString() } as ClassItem
    private Map<String, RegularClassItem> regularItems = CoretexPluginContext.instance.regularItems

    @Override
    List<RegularTypeInsert> build() {
        println "Generate essential data for ${essentialItem.code}"
        List<RegularTypeInsert> inserts = []
        regularItems.findAll { it.key == it.value.code }
                    .each { regularItem ->
            inserts.add(genInsert(essentialItem, regularItem.value))
        }

        return inserts
    }

    @Override
    List<RegularTypeInsert> build(RegularClassItemDiffDataHolder dataHolder) {
        List<RegularTypeInsert> inserts = []
            inserts.add(genInsert(essentialItem, dataHolder.item))
        return inserts
    }

    private static RegularTypeInsert genInsert(ClassItem essentialItem, RegularClassItem item) {
        RegularTypeInsert insert = new RegularTypeInsert()
        insert.essentialItem = essentialItem
        insert.data = new RegularTypeInsert.RegularTypeWrapper(item)
        return insert
    }
}
