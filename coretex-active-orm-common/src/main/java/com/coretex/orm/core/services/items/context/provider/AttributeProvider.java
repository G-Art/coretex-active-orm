package com.coretex.orm.core.services.items.context.provider;

import com.coretex.orm.core.services.items.context.ItemContext;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 17-02-2017
 */
@FunctionalInterface
public interface AttributeProvider {

    <T> T getValue(String attributeName, ItemContext ctx);
}
