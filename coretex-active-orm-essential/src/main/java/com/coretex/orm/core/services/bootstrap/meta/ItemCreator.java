package com.coretex.orm.core.services.bootstrap.meta;

import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.context.impl.ItemContextBuilder;
import com.coretex.items.core.GenericItem;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.Validate.isTrue;

public class ItemCreator {

	private MetaDataContext dataContext;

	private MetaItemContext itemContext;

	public ItemCreator(MetaDataContext dataContext, MetaItemContext itemContext) {
		this.dataContext = dataContext;
		this.itemContext = itemContext;
	}

	public <T extends GenericItem> T convert(UUID itemUuid, Class<T> metaClass) {
		isTrue(nonNull(itemUuid), "Item uuid must be not null");
		isTrue(nonNull(metaClass), "Item class must be not null");

		ItemContext metaItemContext = ItemContextBuilder.createMetaTypeContextBuilder(metaClass)
				.addProvider(new MetaAttributeValueProvider())
				.setUuid(itemUuid)
				.build();

		return createInstance(metaClass, metaItemContext);
	}

	private <T extends GenericItem> T createInstance(Class<T> metaClass, ItemContext metaItemContext) {
		try {
			return ConstructorUtils.invokeConstructor(metaClass, metaItemContext);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			throw new IllegalStateException(e);
		}
	}

	public MetaDataContext getDataContext() {
		return dataContext;
	}

	public MetaItemContext getItemContext() {
		return itemContext;
	}


}
