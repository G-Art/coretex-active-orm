package com.coretex.build.data.db.builder.inserts

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.EnumValueDiffDataHolder
import com.coretex.build.data.db.inserts.RelationInsert
import com.coretex.build.data.items.*
import com.coretex.common.annotation.EssentialDataItem

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItem(itemCode = ENUM_VALUE_OWNER_RELATION)
class MetaEnumValueOwnerRelationDataBuilder implements InsertDataBuilder<RelationInsert, EnumValueDiffDataHolder> {

    private List<AbstractItem> items = CoretexPluginContext.instance.items

    private RelationItem metaRelation

    private ClassItem metaEnumType

    private ClassItem metaEnumValueType

    MetaEnumValueOwnerRelationDataBuilder() {
        metaRelation = items.findAll { it.class in RelationItem }
                .collect { it as RelationItem }
                .find { it.code == ENUM_VALUE_OWNER_RELATION.toString() }
        metaEnumType = items.findAll { it.class in ClassItem }
                .collect { it as ClassItem }
                .find { it.code == META_ENUM_TYPE.toString() }
        metaEnumValueType = items.findAll { it.class in ClassItem }
                .collect { it as ClassItem }
                .find { it.code == META_ENUM_VALUE_TYPE.toString() }
    }

    @Override
    List<RelationInsert> build() {
        List<RelationInsert> inserts = []
        items.findAll { item -> item.class in EnumItem }
                .collect { it as EnumItem }
                .each { enumType -> enumType.values.forEach { enumValue -> inserts.add(genInsert(enumValue, enumType)) }
                }
        return inserts
    }

    @Override
    List<RelationInsert> build(EnumValueDiffDataHolder dataHolder) {
        List<RelationInsert> inserts = []
        if (dataHolder.itemClass in EnumValue) {
             inserts.add(genInsert(dataHolder.item, dataHolder.item.owner))
        }
        return inserts
    }

    private RelationInsert genInsert(EnumValue source, EnumItem target) {
        RelationInsert insert = new RelationInsert()
        insert.essentialItem = metaRelation
        RelationInsert.RelationData relationData = insert.createRelationData()
        relationData.source = source
        relationData.target = target
        relationData.sourceType = metaEnumValueType
        relationData.targetType = metaEnumType
        insert.data = relationData
        return insert
    }
}
