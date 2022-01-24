package com.coretex.build.data.db.updates.columns

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.updates.Update
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.Item

import static org.apache.commons.lang.StringEscapeUtils.escapeSql
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
abstract class Column<I extends ItemDiffDataHolder<? extends Item>, U extends Update> implements ColumnDiffValueAccessor<I> {

    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    private AbstractItem essentialItem
    U update

    @Override
    AbstractItem getEssentialItem() {
        return essentialItem
    }

    void setEssentialItem(AbstractItem essentialItem) {
        this.essentialItem = essentialItem
    }

    boolean hasDiff(){
        def name = getColumnName()
        def object = escapeSql(String.valueOf(data.dbData.get(name.toLowerCase())))
        def value = String.valueOf(getValue())
        if(value.toString().startsWith("'")){
            return !Objects.equals(value.toLowerCase(), "'${object}'".toString().toLowerCase())
        }
        return !Objects.equals(value.toLowerCase(), object.toLowerCase())
    }

    String getNullValue() {
        return buildContext.dbDialect.typeAppropriateService.nullValue
    }

    abstract Object getValue()
}