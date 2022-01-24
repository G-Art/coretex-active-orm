package com.coretex.build.data.db.builder

import com.coretex.build.data.db.builder.inserts.InsertDataBuilder
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.inserts.Insert
import com.coretex.build.data.items.Item
import com.coretex.common.annotation.EssentialDataItem
import com.coretex.common.utils.ReflectionUtils
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import static java.util.Objects.requireNonNull
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */

class EssentialInsertDataBuilder {

    private static final Logger LOG = Logging.getLogger(EssentialInsertDataBuilder)

    private static final String ACTIONS_PATH = 'com.coretex.build.data.db'

    private Set<Class<InsertDataBuilder>> dataBuilders

    private EssentialDataItem.EssentialItem itemTypeCode

    EssentialInsertDataBuilder() {
        this.dataBuilders = ReflectionUtils.findClassesAnnotatedWith(ACTIONS_PATH, EssentialDataItem, { it.findAll({it in InsertDataBuilder}) })
    }

    EssentialInsertDataBuilder itemType(EssentialDataItem.EssentialItem itemTypeCode) {
        this.itemTypeCode = itemTypeCode
        return this
    }

    List<Insert> build() {
        requireNonNull(itemTypeCode, 'Item type code shouldn\'t be null')
        LOG.info("Looking for a builder for essential data item type [${itemTypeCode}]")
        InsertDataBuilder dataBuilder = findBuilder()
        return dataBuilder.build()
    }

    List<Insert> build(ItemDiffDataHolder<? extends Item> dataHolder) {
        requireNonNull(itemTypeCode, 'Item type code shouldn\'t be null')
        LOG.info("Looking for a builder for essential data item type [${itemTypeCode}]")
        InsertDataBuilder dataBuilder = findBuilder()
        return dataBuilder.build(dataHolder)
    }

    private InsertDataBuilder findBuilder() {
        Class<InsertDataBuilder> builder =
                dataBuilders.find {
                    it.getAnnotation(EssentialDataItem).itemCode() == itemTypeCode
                }
        if (!builder) {
            throw new ClassNotFoundException("Essential data builder for [${itemTypeCode}] item type is not found")
        }
        return dataBuilders.find { it.getAnnotation(EssentialDataItem).itemCode() == itemTypeCode }.newInstance()
    }

}
