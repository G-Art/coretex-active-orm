package com.coretex.build.data.db.builder


import com.coretex.build.data.db.builder.remove.RemoveDataBuilder
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.removes.Remove
import com.coretex.build.data.items.Item
import com.coretex.common.annotation.EssentialDataItem
import com.coretex.common.utils.ReflectionUtils
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import static java.util.Objects.requireNonNull

class EssentialRemoveDataBuilder {

    private static final Logger LOG = Logging.getLogger(EssentialRemoveDataBuilder)

    private static final String ACTIONS_PATH = 'com.coretex.build.data.db'

    private Set<Class<RemoveDataBuilder>> dataBuilders

    private EssentialDataItem.EssentialItem itemTypeCode

    EssentialRemoveDataBuilder() {
        this.dataBuilders = ReflectionUtils.findClassesAnnotatedWith(ACTIONS_PATH, EssentialDataItem, { it.findAll({it in RemoveDataBuilder}) })
    }

    EssentialRemoveDataBuilder itemType(EssentialDataItem.EssentialItem itemTypeCode) {
        this.itemTypeCode = itemTypeCode
        return this
    }

    List<Remove> build(ItemDiffDataHolder<? extends Item> dataHolder) {
        requireNonNull(itemTypeCode, 'Item type code shouldn\'t be null')
        LOG.info("Looking for a builder for essential data item type [${itemTypeCode}]")
        RemoveDataBuilder dataBuilder = findBuilder()
        return dataBuilder.build(dataHolder)
    }

    private RemoveDataBuilder findBuilder() {
        Class<RemoveDataBuilder> builder =
                dataBuilders.find {
                    it.getAnnotation(EssentialDataItem).itemCode() == itemTypeCode
                }
        if (!builder) {
            throw new ClassNotFoundException("Essential data builder for [${itemTypeCode}] item type is not found")
        }
        return dataBuilders.find { it.getAnnotation(EssentialDataItem).itemCode() == itemTypeCode }.newInstance()
    }

}
