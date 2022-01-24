package com.coretex.build.converters.impl

import com.coretex.build.converters.Converter
import com.coretex.build.data.db.Table
import com.coretex.build.data.db.TableField
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.RelationItem
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.common.DbDialect
import com.coretex.common.utils.BuildUtils
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class RelationItemToTableConverter extends AbstractConverter<RelationItem, Table> {

    private Converter<Attribute<Item>, TableField> attributeConverter

    RelationItemToTableConverter(DbDialect dialect) {
        super(dialect)
    }

    @Override
    Table doConverting(RelationItem relationItem) {
        Table table = target
        table.setName(relationItem.tableName)
        table.addFields(createFields(relationItem.parentItem))

        relationItem.implicitAttributes.each { attr ->
            table.addField(attributeConverter.convert(attr))
        }
        table.setItem(relationItem)

        return table
    }

    void setAttributeConverter(Converter<Attribute<Item>, TableField> attributeConverter) {
        this.attributeConverter = attributeConverter
    }

    private Set<TableField> createFields(ClassItem classItem) {
        Set<TableField> fieldList = [] as Set

        BuildUtils.collectAllAttributesForItem(classItem)
                .findAll { (it.owner as ClassItem).relations[it.name] == null }
                .each { Attribute attr -> addField(attributeConverter.convert(attr), fieldList) }

        return fieldList
    }

    private static addField(TableField field, Set<TableField> fields) {
        if (field) {
            fields << field
        }
    }
}
