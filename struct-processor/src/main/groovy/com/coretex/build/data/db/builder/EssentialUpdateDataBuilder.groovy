package com.coretex.build.data.db.builder

import com.coretex.build.data.db.builder.update.UpdateDataBuilder
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.updates.Update
import com.coretex.build.data.items.Item
import com.coretex.common.annotation.EssentialDataItem
import com.coretex.common.utils.ReflectionUtils
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import static java.util.Objects.requireNonNull

class EssentialUpdateDataBuilder {

    private static final Logger LOG = Logging.getLogger(EssentialUpdateDataBuilder)

    private static final String ACTIONS_PATH = 'com.coretex.build.data.db'

    private Set<Class<UpdateDataBuilder>> dataBuilders

    private EssentialDataItem.EssentialItem itemTypeCode

    EssentialUpdateDataBuilder() {
        this.dataBuilders = ReflectionUtils.findClassesAnnotatedWith(ACTIONS_PATH, EssentialDataItem, { it.findAll({it in UpdateDataBuilder}) })
    }

    EssentialUpdateDataBuilder itemType(EssentialDataItem.EssentialItem itemTypeCode) {
        this.itemTypeCode = itemTypeCode
        return this
    }

    List<Update> build(ItemDiffDataHolder<? extends Item> dataHolder) {
        requireNonNull(itemTypeCode, 'Item type code shouldn\'t be null')
        LOG.info("Looking for a builder for essential data item type [${itemTypeCode}]")
        UpdateDataBuilder dataBuilder = findBuilder()
        return dataBuilder.build(dataHolder)
    }

    private UpdateDataBuilder findBuilder() {
        Class<UpdateDataBuilder> builder =
                dataBuilders.find {
                    it.getAnnotation(EssentialDataItem).itemCode() == itemTypeCode
                }
        if (!builder) {
            throw new ClassNotFoundException("Essential data builder for [${itemTypeCode}] item type is not found")
        }
        return dataBuilders.find { it.getAnnotation(EssentialDataItem).itemCode() == itemTypeCode }.newInstance()
    }

}
