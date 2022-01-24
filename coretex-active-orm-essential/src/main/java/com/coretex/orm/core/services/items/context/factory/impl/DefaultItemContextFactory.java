package com.coretex.orm.core.services.items.context.factory.impl;

import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.context.factory.ItemContextFactory;
import com.coretex.orm.core.services.items.context.impl.ItemContextBuilder;
import com.coretex.orm.core.services.items.context.provider.AttributeProvider;
import com.coretex.orm.meta.AbstractGenericItem;

import java.util.UUID;

import static com.coretex.orm.core.services.items.context.impl.ItemContextBuilder.createDefaultContextBuilder;

public class DefaultItemContextFactory implements ItemContextFactory {

	private AttributeProvider attributeProvider;

	public DefaultItemContextFactory(AttributeProvider attributeProvider) {
		this.attributeProvider = attributeProvider;
	}

	@Override
	public ItemContext create(Class<? extends AbstractGenericItem> itemClass) {
		return create(createDefaultContextBuilder(itemClass));
	}

	@Override
	public ItemContext create(String typeCode) {
		return create(createDefaultContextBuilder(typeCode));
	}

	@Override
	public ItemContext create(Class<? extends AbstractGenericItem> itemClass, UUID uuid) {
		return create(createDefaultContextBuilder(itemClass).setUuid(uuid));
	}

	private ItemContext create(ItemContextBuilder builder) {
		builder.addProvider(attributeProvider);
		return builder.build();
	}

}
