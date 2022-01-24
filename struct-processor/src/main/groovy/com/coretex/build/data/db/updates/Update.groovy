package com.coretex.build.data.db.updates

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.DbCommands
import com.coretex.build.data.db.diff.dataholders.ItemDiffDataHolder
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.RelationItem
import com.coretex.build.services.QueryFactory
import com.coretex.common.DbDialect
import com.coretex.common.annotation.EssentialDataItemColumn
import com.coretex.common.utils.ReflectionUtils

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*
import static com.coretex.common.utils.BuildUtils.collectAllAttributesForItem

abstract class Update<T extends ItemDiffDataHolder<? extends Item>> implements DbCommands {
    private static final String ACTIONS_PATH = 'com.coretex.build.data.db.updates'
    AbstractItem essentialItem
    T data

    private DbDialect dialect = CoretexPluginContext.instance.dbDialect

    List<Column> getColumns() {
        Set<Class<Column>> classList = ReflectionUtils
                .findClassesAnnotatedWith(ACTIONS_PATH, EssentialDataItemColumn)
        if (classList) {
            return classList.findAll { isColumnMatch(it) }
                    .collect {
                        Column col = it.newInstance()
                        col.essentialItem = essentialItem
                        col.data = data
                        col.update = this
                        return col
                    }.findAll {it.hasDiff()}
                    .sort { l, r -> l.columnName == 'uuid' ? 1 : r.columnName == 'uuid' ? -1 : String.compare(l.columnName, r.columnName) }
        }
        return []
    }

    boolean isColumnMatch(Class<Column> columnClass) {
        EssentialDataItemColumn annotation = columnClass.getAnnotation(EssentialDataItemColumn)
        if (!(Void == annotation.supportedItem() || data.item.class.isAssignableFrom(annotation.supportedItem()))) {
            return false
        }
        return (isGeneric(annotation) || isRelationType(annotation) || isTableForThisType(annotation)) && hasAttribute(annotation)

    }

    private boolean isGeneric(EssentialDataItemColumn annotation) {
        annotation.itemCode() == FOR_ALL
    }

    private boolean isRelationType(EssentialDataItemColumn annotation) {
        essentialItem.class in RelationItem && annotation.itemCode() == META_RELATION_TYPE
    }

    private boolean isTableForThisType(EssentialDataItemColumn annotation) {
        annotation.itemCode() == fromString(essentialItem.code)
    }

    private boolean hasAttribute(EssentialDataItemColumn annotation) {
        if (essentialItem.class in ClassItem) {
            return collectAllAttributesForItem(essentialItem as ClassItem)*.name.contains(annotation.columnName())
        }

        if (essentialItem.class in RelationItem) {
            if ('source' == annotation.columnName() || 'target' == annotation.columnName()) {
                return true
            }
            return collectAllAttributesForItem((essentialItem as RelationItem).metaTypeClass)*.name.contains(annotation.columnName())
        }

        return false
    }

    QueryFactory getQueryFactory(){
        return dialect.queryFactory
    }
}
