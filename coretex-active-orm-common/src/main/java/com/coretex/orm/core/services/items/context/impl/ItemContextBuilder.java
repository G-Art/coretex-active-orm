package com.coretex.orm.core.services.items.context.impl;

import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.context.provider.AttributeProvider;
import com.coretex.orm.core.services.items.exceptions.ItemCreationException;
import com.coretex.orm.meta.AbstractGenericItem;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.apache.commons.lang3.reflect.ConstructorUtils.invokeConstructor;

public class ItemContextBuilder {

	public static ItemContextBuilder createDefaultContextBuilder(Class<? extends AbstractGenericItem> itemClass) {
		return new ItemContextBuilder(getMetaTypeCode(itemClass), DefaultItemContextImpl.class);
	}

	public static ItemContextBuilder createMetaTypeContextBuilder(Class<? extends AbstractGenericItem> itemClass) {
		return new ItemContextBuilder(getMetaTypeCode(itemClass), MetaItemContext.class);
	}

	public static ItemContextBuilder createDefaultContextBuilder(String typeCode) {
		return new ItemContextBuilder(typeCode, DefaultItemContextImpl.class);
	}

	private String typeCode;

	private UUID uuid;

	private AttributeProvider provider;

	private Class<? extends ItemContext> toBuild;

	private ItemContextBuilder(String typeCode, Class<? extends ItemContext> toBuild) {
		checkArgument(isNoneBlank(typeCode), "Creating item context requires type code");
		this.typeCode = typeCode;
		this.toBuild = toBuild;
	}

	public ItemContextBuilder setUuid(UUID uuid) {
		checkArgument(nonNull(uuid), "Uuid must be not null");
		this.uuid = uuid;
		return this;
	}

	public ItemContextBuilder addProvider(AttributeProvider provider) {
		checkArgument(nonNull(provider), "Attribute provider must be not null");
		this.provider = provider;
		return this;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public AttributeProvider getProvider() {
		return provider;
	}

	public ItemContext build() {
		try {
			return invokeConstructor(toBuild, this);
		} catch (ReflectiveOperationException e) {
			throw new ItemCreationException("Can't build context for item", e);
		}
	}

	private static String getMetaTypeCode(Class<? extends AbstractGenericItem> itemClass) {
		checkArgument(nonNull(itemClass), "Item class is required for building item context");

		Field field = FieldUtils.getField(itemClass, "ITEM_TYPE");
		try {
			return (String) FieldUtils.readStaticField(field);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
