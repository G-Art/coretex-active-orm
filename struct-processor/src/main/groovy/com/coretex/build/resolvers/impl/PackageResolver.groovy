package com.coretex.build.resolvers.impl

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.resolvers.Resolver

import static com.coretex.Constants.PROPERTY_PACKAGE

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 */
class PackageResolver implements Resolver {

    private CoretexPluginContext context = CoretexPluginContext.instance

    @Override
    void resolve(AbstractItem item) {
        if (item.package == null) {
            item.setPackage("${context.getBuildProperty(PROPERTY_PACKAGE)}.${item.itemTypeFolder}.${item.ownerModuleName.toLowerCase()}")
        }
    }

    @Override
    void resolve(List<AbstractItem> items) {
        items.each { resolve(it) }
    }
}