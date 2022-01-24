package com.coretex.build.data.db.inserts.columns

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.inserts.RowItem
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RelationItem
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.Constants.COLUMN_PREFIX
import static com.coretex.common.utils.BuildUtils.collectAllAttributesForItem

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
abstract class Column<T extends RowItem> {

    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    private AbstractItem essentialItem
    private T data

    AbstractItem getEssentialItem() {
        return essentialItem
    }

    void setEssentialItem(AbstractItem essentialItem) {
        this.essentialItem = essentialItem
    }

    T getData() {
        return data
    }

    void setData(T data) {
        this.data = data
    }

    String getColumnName() {
        EssentialDataItemColumn annotation = this.class.getAnnotation(EssentialDataItemColumn)
        if (essentialItem.class in RelationItem) {
            if ('source' == annotation.columnName() || 'target' == annotation.columnName()) {
                return COLUMN_PREFIX + annotation.columnName()
            }
            Attribute attribute = collectAllAttributesForItem((essentialItem as RelationItem).metaTypeClass).find {
                it.name == annotation.columnName()
            }
            return attribute.columnName
        }
        if (essentialItem.class in ClassItem) {
            Attribute attribute = collectAllAttributesForItem(essentialItem as ClassItem).find {
                it.name == annotation.columnName()
            }
            return attribute.columnName
        }

        return null

    }

    String getNullValue() {
        return buildContext.dbDialect.typeAppropriateService.nullValue
    }

    abstract Object getRawValue()

    Object getValue() {
        return String.valueOf(getRawValue())
    }
}