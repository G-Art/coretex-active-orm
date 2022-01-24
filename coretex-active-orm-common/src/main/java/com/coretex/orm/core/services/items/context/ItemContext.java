package com.coretex.orm.core.services.items.context;

import com.coretex.orm.core.AbstractCoretexContextProvider;
import com.coretex.orm.core.services.items.context.impl.ItemContextBuilder;
import com.coretex.orm.core.services.items.context.provider.AttributeProvider;
import com.google.common.base.Preconditions;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 * create by 13-02-2016
 */
public abstract class ItemContext implements Serializable, Cloneable {

	private String typeCode;

	protected UUID uuid;

	private transient AttributeProvider provider;

	public ItemContext(ItemContextBuilder builder) {
		Preconditions.checkArgument(nonNull(builder), "Context builder is required");
		typeCode = builder.getTypeCode();
		uuid = builder.getUuid();
		provider = builder.getProvider();
	}

	protected Object readResolve() throws ObjectStreamException {
		provider = AbstractCoretexContextProvider.getInstance().getDefaultAttributeProvider();
		return this;
	}

	public abstract Collection<String> loadedAttributes();

	public abstract boolean isNew();

	public abstract boolean isExist();

	public abstract <T> T getOriginValue(String attributeName);

	public abstract <T> T getOriginLocalizedValue(String attributeName);

	public abstract <T> T getOriginLocalizedValue(String attributeName, Locale locale);

	public abstract <T> Map<Locale, T> getOriginLocalizedValues(String attributeName);

	public abstract <T> T getValue(String attributeName);

	public abstract <T> T getLocalizedValue(String attributeName);

	public abstract <T> Map<Locale, T> getLocalizedValues(String attributeName);

	public abstract <T> T getLocalizedValue(String attributeName, Locale locale);

	public abstract <T> void setValue(String attributeName, T value);

	public abstract <T> void setLocalizedValue(String attributeName, T value);

	public abstract <T> void setLocalizedValue(String attributeName, T value, Locale locale);

	public abstract void initValue(String attributeName, Object initialValue);

	public abstract void flush();

	public abstract void refresh();

	public abstract boolean isSystemType();

	@Override
	public ItemContext clone() throws CloneNotSupportedException {
		var clone = (ItemContext)super.clone();
		clone.uuid = this.uuid;
		clone.typeCode = this.typeCode;
		clone.provider = this.provider;
		return clone;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		Preconditions.checkNotNull(uuid, "Uuid can't be null");
		this.uuid = uuid;
	}

	public AttributeProvider getProvider() {
		return provider;
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isDirty(String attributeName) {
		return false;
	}
}
