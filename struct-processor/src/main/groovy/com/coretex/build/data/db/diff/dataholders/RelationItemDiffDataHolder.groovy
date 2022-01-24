package com.coretex.build.data.db.diff.dataholders

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.converters.impl.AbstractConverter
import com.coretex.build.converters.impl.AttributeToTableFieldConverter
import com.coretex.build.converters.impl.RelationItemToTableConverter
import com.coretex.build.data.db.Table
import com.coretex.build.data.items.RelationItem

class RelationItemDiffDataHolder extends AbstractClassItemDiffDataHolder<RelationItem> {

    private CoretexPluginContext buildContext = CoretexPluginContext.instance
    private RelationItemToTableConverter relationItemToTableConverter

    RelationItemDiffDataHolder() {
        this.relationItemToTableConverter = new RelationItemToTableConverter(buildContext.dbDialect)
        this.relationItemToTableConverter.setAttributeConverter(new AttributeToTableFieldConverter(buildContext.dbDialect))
    }

    @Override
    AbstractConverter<RelationItem, Table> converter() {
        return relationItemToTableConverter
    }
}
