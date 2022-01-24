package com.coretex.build.resolvers.impl

import com.coretex.build.data.items.*
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.build.data.items.traits.JavaClass
import com.coretex.build.resolvers.Resolver

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 */
class ImportResolver implements Resolver {

    @Override
    void resolve(AbstractItem item) {
        if (item.class == ClassItem) {
            generateImportForItem(item, item.parentItem)
            item.attributes.each this.&generateImportForAttribute
        }

        if (item.class == RelationItem) {
            generateImportForItem(item, item.parentItem)
            item.parentItem.attributes.each {
                generateImportForAttribute(item, it)
            }
            item.implicitAttributes.each {
                generateImportForAttribute(item, it)
            }
        }
    }

    @Override
    void resolve(List<AbstractItem> items) {
        items.each { resolve(it) }
    }

    private void generateImportForItem(JavaClass classItem, Item compositeItem) {
        if (classItem == null || compositeItem == null) {
            return
        }

        if (compositeItem.class in RegularClassItem && !((compositeItem as RegularClassItem).ignored)) {
            generateImportForRegularClassItem(classItem, compositeItem as RegularClassItem)
        }
        if (compositeItem.class in AbstractItem && compositeItem.package != classItem.package) {
            generateImportForAbstractItem(classItem, compositeItem as AbstractItem)
        }
    }

    private static void generateImportForRegularClassItem(JavaClass item, RegularClassItem regularClassItem) {
        item.addImport(regularClassItem.code)
    }

    private static void generateImportForAbstractItem(JavaClass item, AbstractItem abstractItem) {
        item.addImport("${abstractItem.package}.${abstractItem.typeName}")
    }

    private void generateImportForAttribute(Attribute<Item> attribute) {
        generateImportForAttribute(attribute.owner, attribute)
    }

    private void generateImportForAttribute(JavaClass item, Attribute<Item> attribute) {
        if (attribute.containerType) {
            item.addImport(attribute.containerType.name)
        }
        if (attribute.localized){
            item.addImport(Locale.class.getName())
            item.addImport(Map.class.getName())
        }
        generateImportForItem(item, attribute.type)
    }
}