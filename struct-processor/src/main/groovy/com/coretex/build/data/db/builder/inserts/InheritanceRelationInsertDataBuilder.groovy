package com.coretex.build.data.db.builder.inserts

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.inserts.RelationInsert
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RelationItem
import com.coretex.build.data.items.traits.Insertable
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.INHERITANCE_RELATION
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.META_TYPE

@EssentialDataItem(itemCode = INHERITANCE_RELATION)
class InheritanceRelationInsertDataBuilder implements InsertDataBuilder<RelationInsert, AbstractClassItemDiffDataHolder> {

    private List<AbstractItem> items

    private RelationItem inheritanceRelation

    private ClassItem metaType

    InheritanceRelationInsertDataBuilder() {
        items = CoretexPluginContext.instance.items
        inheritanceRelation = items.findAll { it.class in RelationItem }
                .collect { it as RelationItem }
                .find { it.code == INHERITANCE_RELATION.toString() }
        metaType = items.find { it.code == META_TYPE.toString() } as ClassItem
    }

    @Override
    List<RelationInsert> build() {
        List<RelationInsert> inserts = []

        items.findAll { it.class in ClassItem && (it as ClassItem).parentItem }
                .collect { it as ClassItem }
                .collect(this.&convertClassItem)
                .each(inserts.&add)
        items.findAll { it.class in RelationItem }
                .collect { it as RelationItem }
                .collect(this.&convertRelationItem)
                .each(inserts.&add)
        return inserts
    }

    @Override
    List<RelationInsert> build(AbstractClassItemDiffDataHolder dataHolder) {
        List<RelationInsert> inserts = []
        if(dataHolder.isClass() && (dataHolder.item as ClassItem).parentItem){
            inserts.addAll(convertClassItem(dataHolder.item as ClassItem))
        }
        if(dataHolder.isRelation()){
            inserts.addAll(convertRelationItem(dataHolder.item as RelationItem))
        }
        return inserts
    }

    private RelationInsert convertClassItem(ClassItem classItem) {
        return genInsert(classItem, metaType, classItem.parentItem)
    }

    private RelationInsert convertRelationItem(RelationItem relationItem) {
        return genInsert(relationItem, metaType, metaType.parentItem)
    }

    private RelationInsert genInsert(Insertable source, ClassItem sourceType, Insertable target) {
        RelationInsert insert = new RelationInsert()
        insert.essentialItem = inheritanceRelation
        RelationInsert.RelationData relationData = insert.createRelationData()
        relationData.source = source
        relationData.target = target
        relationData.sourceType = sourceType
        relationData.targetType = metaType
        insert.data = relationData
        return insert
    }

}
