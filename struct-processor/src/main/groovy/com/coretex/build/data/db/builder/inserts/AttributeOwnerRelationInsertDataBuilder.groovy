package com.coretex.build.data.db.builder.inserts

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.inserts.RelationInsert
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RelationItem
import com.coretex.build.data.items.traits.Insertable
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*

@EssentialDataItem(itemCode = ATTRIBUTE_OWNER_RELATION)
class AttributeOwnerRelationInsertDataBuilder implements InsertDataBuilder<RelationInsert, AttributeItemDiffDataHolder> {

    private List<AbstractItem> items

    private RelationItem attributeOwnerRelation

    private ClassItem metaType

    private ClassItem metaAttributeType

    AttributeOwnerRelationInsertDataBuilder() {
        items = CoretexPluginContext.instance.items
        attributeOwnerRelation = items.findAll { it.class in RelationItem }
                .collect { it as RelationItem }
                .find { it.code == ATTRIBUTE_OWNER_RELATION.toString() }
        metaType = items.find { it.code == META_TYPE.toString() } as ClassItem
        metaAttributeType = items.find { it.code == META_ATTRIBUTE_TYPE.toString() } as ClassItem
    }

    @Override
    List<RelationInsert> build() {
        List<RelationInsert> inserts = []

        items.findAll { it.class in ClassItem  }
                .collect { it as ClassItem }
                .collect(this.&convertClassItem)
                .each(inserts.&addAll)

        items.findAll { it.class in RelationItem  }
                .collect { it as RelationItem }
                .collect(this.&convertRelationItem)
                .each(inserts.&addAll)

        Insertable target = items.find { it.class in ClassItem && (it.code == 'Generic') }
        inserts.add(genInsert(CoretexPluginContext.instance.uuidAttribute, target, metaType))
        return inserts
    }

    @Override
    List<RelationInsert> build(AttributeItemDiffDataHolder abstractItem) {
        List<RelationInsert> inserts = []
        def owner = abstractItem.item.owner
        inserts.add(genInsert(abstractItem.item, abstractItem.item.owner, owner.class in ClassItem ? metaType : (owner as RelationItem).metaTypeClass))
        return inserts
    }

    private List<RelationInsert> convertClassItem(ClassItem classItem) {
        return classItem.attributes
                .collect { genInsert(it, classItem, metaType) }
    }

    private List<RelationInsert> convertRelationItem(RelationItem relationItem) {
        return relationItem.implicitAttributes.collect { genInsert(it, relationItem, relationItem.metaTypeClass) }
    }

    private RelationInsert genInsert(Insertable source, Insertable target, ClassItem targetType) {
        RelationInsert insert = new RelationInsert()
        insert.essentialItem = attributeOwnerRelation
        RelationInsert.RelationData relationData = insert.createRelationData()
        relationData.source = source
        relationData.target = target
        relationData.sourceType = metaAttributeType
        relationData.targetType = targetType
        insert.data = relationData
        return insert
    }
}
