package com.coretex.build.converters.impl

import com.coretex.Constants
import com.coretex.build.data.db.TableField
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.RegularClassItem
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.common.DbDialect
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class AttributeToTableFieldConverter extends AbstractConverter<Attribute<Item>, TableField> {



    AttributeToTableFieldConverter(DbDialect dialect) {
        super(dialect)
    }

    @Override
    TableField doConverting(Attribute<Item> attribute) {
        TableField tableField = target
        tableField.setName(attribute.columnName)
        tableField.setJavaClassType(attribute.type.code)
        if (attribute.type?.class in RegularClassItem) {
            tableField.setSqlType(appropriateService.getBasicDataType(attribute as Attribute<RegularClassItem>))
        }
        if (attribute.type?.class in AbstractItem) {
            tableField.setSqlType(appropriateService.relationDataType)
        }
        if(attribute.index){
            attribute.index.each{tableField.addIndexName(Constants.INDEX_PREFIX + it)}
        }

        tableField.setAttribute(attribute)
        return tableField
    }

}
