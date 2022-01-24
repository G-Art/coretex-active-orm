package com.coretex.build.resolvers

import com.coretex.build.data.items.AbstractItem

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
interface Resolver {

    void resolve(AbstractItem item)

    void resolve(List<AbstractItem> items)

}