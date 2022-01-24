package com.coretex.orm.core.services.items.context.factory;

import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.meta.AbstractGenericItem;

import java.util.UUID;

public interface ItemContextFactory {

	ItemContext create(Class<? extends AbstractGenericItem> itemClass);

	ItemContext create(String typeCode);

	ItemContext create(Class<? extends AbstractGenericItem> itemClass, UUID uuid);
}
