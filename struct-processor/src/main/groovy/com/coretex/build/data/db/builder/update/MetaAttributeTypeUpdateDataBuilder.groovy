package com.coretex.build.data.db.builder.update

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.db.updates.MetaAttributeTypeUpdate
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
class MetaAttributeTypeUpdateDataBuilder implements UpdateDataBuilder<MetaAttributeTypeUpdate, AttributeItemDiffDataHolder> {

    private static final Logger LOG = Logging.getLogger(MetaAttributeTypeUpdateDataBuilder)

    private List<AbstractItem> items = CoretexPluginContext.instance.items

    private ClassItem essentialItem
    private ClassItem itemType
    private ClassItem enumType
    private ClassItem regularType
    private ClassItem relationType

    MetaAttributeTypeUpdateDataBuilder() {
        this.essentialItem = items.find { it.code == META_ATTRIBUTE_TYPE.toString() } as ClassItem
        this.itemType = items.find { it.code == META_TYPE.toString() } as ClassItem
        this.enumType = items.find { it.code == META_ENUM_TYPE.toString() } as ClassItem
        this.regularType = items.find { it.code == REGULAR_TYPE.toString() } as ClassItem
        this.relationType = items.find { it.code == META_RELATION_TYPE.toString() } as ClassItem
    }


    @Override
    List<MetaAttributeTypeUpdate> build(AttributeItemDiffDataHolder diffDataHolder) {
        def abstractItem = diffDataHolder.item
        List<MetaAttributeTypeUpdate> updates = []
        updates.add(genUpdate(diffDataHolder, getEssentialValueType(abstractItem), abstractItem))
        return updates
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

    private MetaAttributeTypeUpdate genUpdate(AttributeItemDiffDataHolder diffDataHolder, ClassItem essentialValueType, Attribute<Item> attribute) {
        MetaAttributeTypeUpdate update = new MetaAttributeTypeUpdate()
        update.essentialItem = essentialItem
        Item type = essentialValueType.code == META_RELATION_TYPE.toString() ?
                (attribute.owner as ClassItem).relations[attribute.name] :
                attribute.type

        update.data = diffDataHolder
        update.type = type
        update.attribute = attribute
        update.essentialValueType = essentialValueType
        return update
    }
}
