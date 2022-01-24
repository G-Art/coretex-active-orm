package com.coretex.build.data.db.diff.dataholders

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.converters.impl.AbstractConverter
import com.coretex.build.converters.impl.AttributeToTableFieldConverter
import com.coretex.build.converters.impl.ClassItemToTableConverter
import com.coretex.build.data.db.Table
import com.coretex.build.data.items.ClassItem

class ClassItemDiffDataHolder extends AbstractClassItemDiffDataHolder<ClassItem> {
    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    private ClassItemToTableConverter classItemToTableConverter

    private boolean hasLocalizedTable = false

    ClassItemDiffDataHolder() {
        this.classItemToTableConverter = new ClassItemToTableConverter(buildContext.dbDialect)
        this.classItemToTableConverter.setAttributeConverter(new AttributeToTableFieldConverter(buildContext.dbDialect))
    }

    @Override
    AbstractConverter<ClassItem, Table> converter() {
        return classItemToTableConverter
    }

    boolean getHasLocalizedTable() {
        return hasLocalizedTable
    }

    void setHasLocalizedTable(boolean hasLocalizedTable) {
        this.hasLocalizedTable = hasLocalizedTable
    }
}
