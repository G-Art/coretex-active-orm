package com.coretex.common.utils

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.attributes.Attribute
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam

import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.sql.Statement

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class BuildUtils {

    static Object retrieveProperty(String property, Object defaultValue) {
        Object result = retrieveProperty(property)
        return result != null ? result : defaultValue
    }

    static Object retrieveProperty(String property) {
        CoretexPluginContext pluginContext = CoretexPluginContext.instance
        if (pluginContext.containBuildProperty(property)) {
            return pluginContext.getBuildProperty(property)
        }
        return null
    }

    static Set<Attribute<Item>> collectAttributesForItem(ClassItem item, InheritanceWay inheritanceWay,
                                                                 @ClosureParams(value = FirstParam) Closure<Set<Attribute<Item>>> closure) {
        Set<Attribute<Item>> attributes = [] as Set
        ClassItem classItem = item

        if (classItem.parentItem != null &&
                (inheritanceWay == InheritanceWay.PARENT || inheritanceWay == null)) {
            attributes.addAll(collectAttributesForItem(classItem.parentItem, InheritanceWay.PARENT, closure))
        }
        if (classItem.childItems != null &&
                (inheritanceWay == InheritanceWay.CHILD || inheritanceWay == null)) {
            classItem.childItems.each {
                if (!it.table) {
                    attributes.addAll(collectAttributesForItem(it, InheritanceWay.CHILD, closure))
                }
            }
        }

        attributes.addAll(closure.call(classItem))

        return attributes
    }

    static Set<Attribute<Item>> collectAllAttributesForItem(ClassItem item) {
        return collectAllAttributesForItem(item, null)
    }

    static Set<Attribute<Item>> collectAllAttributesForItem(ClassItem item, InheritanceWay inheritanceWay) {
        return collectAttributesForItem(item, inheritanceWay) { it.attributes }
    }

    static enum InheritanceWay {
        PARENT, CHILD
    }


    static List<Map<String, Object>> execute(String query, Statement statement) throws SQLException {
        return resultSetToList(statement.executeQuery(query))
    }

    static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData()
        int columns = md.getColumnCount()
        List<Map<String, Object>> list = new ArrayList()
        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<>(columns)
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i))
            }
            list.add(row)
        }

        return list
    }
}
