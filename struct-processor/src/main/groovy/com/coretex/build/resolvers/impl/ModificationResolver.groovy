package com.coretex.build.resolvers.impl

import com.coretex.build.builder.impl.StructFileValidationException
import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.EnumItem
import com.coretex.build.data.items.EnumValue
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.build.resolvers.Resolver
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class ModificationResolver implements Resolver {
    private static final Logger LOG = Logging.getLogger(ModificationResolver)

    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    @Override
    void resolve(AbstractItem item) {
        def itemModifications = buildContext.getModificationItemForModule(item.ownerModuleName, item.code)
        if (Objects.nonNull(itemModifications) && !itemModifications.isEmpty()) {
            LOG.lifecycle("Item [${item.code}] has [${itemModifications.size()}] modifications")
            itemModifications.forEach { itemModification ->
                modify(item, itemModification)
            }

        }
    }

    void modify(AbstractItem item, AbstractItem itemModification) {

        if (item in ClassItem) {
            modifyClassItem(item as ClassItem, itemModification)
        }

        if (item in EnumItem) {
            modifyEnumItem(item as EnumItem, itemModification)
        }

    }

    @Override
    void resolve(List<AbstractItem> items) {
        items.each { resolve(it) }
    }

    private void modifyClassItem(ClassItem classItem, AbstractItem abstractItem) {
        if (abstractItem in ClassItem) {
            def errorAttributes = new ArrayList<Attribute>()
            def attributes = classItem.getAttributes()
            def additionalAttributes = abstractItem.getAttributes()
            additionalAttributes.each { attribute ->
                def existAttribute = attributes.find {
                    return it.code == attribute.code
                }
                if (Objects.nonNull(existAttribute)) {
                    errorAttributes.add(attribute)
                } else {
                    LOG.debug("Add additional attribute [${abstractItem.ownerModuleName}:${abstractItem.code}:${attribute.code}] to [${classItem.ownerModuleName}:${classItem.code}]")
                    attribute.setOwner(classItem)
                    classItem.addAttribute(attribute)
                }
            }

            if (!errorAttributes.isEmpty()) {
                errorAttributes.collect {
                    return "${abstractItem.ownerModuleName}:${abstractItem.code}:${it.code}(type: ${it.typeCode})${it.localized ? '.localized' : ''}"
                }
                throw new StructFileValidationException("Enhance attribute duplications [${errorAttributes.join(', \n')}]")
            }

        }
    }

    private void modifyEnumItem(EnumItem enumItem, AbstractItem abstractItem) {
        if (abstractItem in EnumItem) {
            def errorValues = new ArrayList<EnumValue>()
            def values = enumItem.getValues()
            def additionalValues = abstractItem.getValues()

            additionalValues.each { value ->
                def existValue = values.find {
                    return it.code == value.code
                }
                if (Objects.nonNull(existValue)) {
                    errorValues.add(value)
                } else {
                    LOG.debug("Add additional attribute [${abstractItem.ownerModuleName}:${abstractItem.code}:${value.code}] to [${enumItem.ownerModuleName}:${enumItem.code}]")
                    enumItem.addValue(value)
                }
            }

            if (!errorValues.isEmpty()) {
                errorValues.collect {
                    return "${abstractItem.ownerModuleName}:${abstractItem.code}:${it.code}"
                }
                throw new StructFileValidationException("Enhance attribute duplications [${errorValues.join(', \n')}]")
            }
        }
    }
}
