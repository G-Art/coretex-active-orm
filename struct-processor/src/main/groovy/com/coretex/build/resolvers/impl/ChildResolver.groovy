package com.coretex.build.resolvers.impl

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.resolvers.Resolver

import java.util.stream.Collectors

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class ChildResolver implements Resolver {

    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    @Override
    void resolve(AbstractItem item) {
        if (item.class == ClassItem) {
            item.setChildItems(getChildDependencies(item))
        }
    }

    List<ClassItem> getChildDependencies(ClassItem classItem) {
        List<ClassItem> childClasses = buildContext.items
                .stream()
                .filter { item -> item.class == ClassItem && item.parentItem != null }
                .filter { item -> (((ClassItem) item).parentItem.code == classItem.code) }
                .collect(Collectors.toList())
        return childClasses
    }

    @Override
    void resolve(List<AbstractItem> items) {
        items.each { resolve(it) }
    }
}
