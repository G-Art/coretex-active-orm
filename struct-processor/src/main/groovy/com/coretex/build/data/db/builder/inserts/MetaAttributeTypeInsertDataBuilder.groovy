package com.coretex.build.data.db.builder.inserts

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.inserts.MetaAttributeTypeInsert
import com.coretex.build.data.items.*
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.common.annotation.EssentialDataItem
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */

@EssentialDataItem(itemCode = META_ATTRIBUTE_TYPE)
class MetaAttributeTypeInsertDataBuilder implements InsertDataBuilder<MetaAttributeTypeInsert, AttributeItemDiffDataHolder> {

    private static final Logger LOG = Logging.getLogger(MetaAttributeTypeInsertDataBuilder)

    private List<AbstractItem> items = CoretexPluginContext.instance.items

    private ClassItem essentialItem
    private ClassItem itemType
    private ClassItem enumType
    private ClassItem regularType
    private ClassItem relationType

    MetaAttributeTypeInsertDataBuilder() {
        this.essentialItem = items.find { it.code == META_ATTRIBUTE_TYPE.toString() } as ClassItem
        this.itemType = items.find { it.code == META_TYPE.toString() } as ClassItem
        this.enumType = items.find { it.code == META_ENUM_TYPE.toString() } as ClassItem
        this.regularType = items.find { it.code == REGULAR_TYPE.toString() } as ClassItem
        this.relationType = items.find { it.code == META_RELATION_TYPE.toString() } as ClassItem
    }

    @Override
    List<MetaAttributeTypeInsert> build() {
        LOG.debug "Generate essential data for ${essentialItem.code}"
        List<MetaAttributeTypeInsert> inserts = []
        items.findAll { item -> item.class in ClassItem }.each { classItem ->
            (classItem as ClassItem).attributes.each {
                inserts.add(genInsert(getEssentialValueType(it), it))
            }
            if ((classItem as ClassItem).parentItem == null) {
                inserts.add(genInsert(regularType, CoretexPluginContext.instance.uuidAttribute))
            }
        }

        items.findAll { item -> item.class in RelationItem }.each { classItem ->
            (classItem as RelationItem).implicitAttributes.each {
                inserts.add(genInsert(getEssentialValueType(it), it))
            }
            if ((classItem as RelationItem).parentItem == null) {
                inserts.add(genInsert(regularType, CoretexPluginContext.instance.uuidAttribute))
            }
        }
        return inserts
    }

    @Override
    List<MetaAttributeTypeInsert> build(AttributeItemDiffDataHolder dataHolder) {
        List<MetaAttributeTypeInsert> inserts = []
        inserts.add(genInsert(getEssentialValueType(dataHolder.item), dataHolder.item))
        return inserts
    }

    private ClassItem getEssentialValueType(Attribute<? extends Item> attribute) {
        if (attribute.type.class in RegularClassItem) {
            return regularType
        }
        if (attribute.type.class in EnumItem) {
            return enumType
        }

        if (attribute.owner.class in ClassItem && (attribute.owner as ClassItem).relations[attribute.name]) {
            return relationType
        }

        if (attribute.type.class in ClassItem) {
            return itemType
        }
        throw new IllegalStateException("Can't find apprioriate essential value type for attribute '$attribute.name'")
    }

    private MetaAttributeTypeInsert genInsert(ClassItem essentialValueType, Attribute<Item> attribute) {
        MetaAttributeTypeInsert insert = new MetaAttributeTypeInsert()
        insert.essentialItem = essentialItem
        Item type = essentialValueType.code == META_RELATION_TYPE.toString() ?
                (attribute.owner as ClassItem).relations[attribute.name] :
                attribute.type
        insert.data = new MetaAttributeTypeInsert.AttributeForClassItem(attribute, essentialValueType, type)
        return insert
    }
}
