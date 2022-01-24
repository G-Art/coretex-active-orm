package com.coretex.build.converters.impl

import com.coretex.build.converters.Converter
import com.coretex.build.data.db.Table
import com.coretex.build.data.db.TableField
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.common.DbDialect
import com.coretex.common.utils.BuildUtils

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class ClassItemToTableConverter extends AbstractConverter<ClassItem, Table> {

    private Converter<Attribute<Item>, TableField> attributeConverter

    ClassItemToTableConverter(DbDialect dialect) {
        super(dialect)
    }

    @Override
    Table doConverting(ClassItem classItem) {
        Table table = target
        table.setName(classItem.tableName)
        table.addFields(createFields(classItem))
        table.setItem(classItem)
        table.setLocaleSupportTableRequired(classItem.hasLocalizedAttributes)

        return table
    }

    void setAttributeConverter(Converter<Attribute<Item>, TableField> attributeConverter) {
        this.attributeConverter = attributeConverter
    }

    private Set<TableField> createFields(ClassItem classItem) {
        Set<TableField> fieldList = [] as Set

        //TODO adjust field with the same name collision
        BuildUtils.collectAllAttributesForItem(classItem)
                .findAll { (it.owner as ClassItem).relations[it.name] == null && !it.localized }
                .each { Attribute attr -> addField(attributeConverter.convert(attr), fieldList) }

        return fieldList
    }

    private static addField(TableField field, Set<TableField> fields) {
        if (field) {
            fields << field
        }
    }

}
