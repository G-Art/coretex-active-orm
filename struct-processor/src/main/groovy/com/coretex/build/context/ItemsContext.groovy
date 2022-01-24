package com.coretex.build.context

import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.RegularClassItem
import com.coretex.build.data.items.traits.Adjustable
import com.google.common.collect.HashBasedTable
import com.google.common.collect.Lists
import com.google.common.collect.Table
import org.apache.commons.lang3.RegExUtils
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import static com.coretex.build.parsers.ParserUtils.unquoteString

trait ItemsContext {

    private static final Logger LOG = Logging.getLogger(ItemsContext)
    private List<AbstractItem> items

    Table<String, String, List<AbstractItem>> moduleItemsModifications = HashBasedTable.create()


    private Map<String, RegularClassItem> regularItems


    void addItemModification(AbstractItem item) {
        if (item in Adjustable) {
            def enhance = unquoteString(item.enhance)
            if (moduleItemsModifications.contains(enhance, item.code)) {
                def listOfItems = moduleItemsModifications.get(enhance, item.code)
                listOfItems.add(item)
                moduleItemsModifications.put(enhance, item.code, listOfItems)
            } else {
                def newList = new ArrayList()
                newList.add(item)
                moduleItemsModifications.put(enhance, item.code, newList)
            }
        } else {
            LOG.lifecycle("WARNING: Item [${item.code}] defined in [${item.ownerModuleName}] is not adjastable")
        }

    }

    void addItemModifications(List<AbstractItem> items) {
        items.each { addItemModification(it) }
    }

    List<AbstractItem> getItems() {
        items
    }

    void addItem(AbstractItem item) {
        items.add(item)
    }

    void addItem(List<AbstractItem> items) {
        this.items.addAll(items)
    }

    Map<String, RegularClassItem> getRegularItems() {
        regularItems
    }

    void setRegularItems(Map<String, RegularClassItem> regularItems) {
        this.regularItems = regularItems
    }

    void addRegularItem(Class regularClass) {
        RegularClassItem regularClassItem = new RegularClassItem(regularClass)
        regularItems.put(regularClassItem.code, regularClassItem)
        regularItems.put(regularClassItem.shortCode, regularClassItem)
    }

    List<AbstractItem> getModificationItemForModule(String module, String itemCode) {
        return moduleItemsModifications.get(module, itemCode)
    }

    void cleanUpItemContext() {
        items = []
        regularItems = [:]
        moduleItemsModifications = HashBasedTable.create()
    }
}
