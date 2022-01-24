package com.coretex.orm.core.services.items.context.impl;

import com.coretex.orm.core.services.bootstrap.meta.MetaAttributeValueProvider;
import com.coretex.orm.core.services.items.context.ItemContext;

import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;

public final class MetaItemContext extends ItemContext {

	private static final String UPDATE_INITIAL_TYPE_ERROR_MSG = "Update initial type is unsupported";
	private static final String SERIALIZATION_INITIAL_TYPE_ERROR_MSG = "Serialization/Deserialization initial type is unsupported";

	public MetaItemContext(ItemContextBuilder builder) {
		super(builder);
		checkState(nonNull(getUuid()), "Uuid is mandatory value to build initial type context");
	}

	@Override
	protected Object readResolve() throws ObjectStreamException {
		throw new UnsupportedOperationException(SERIALIZATION_INITIAL_TYPE_ERROR_MSG);
	}

	protected Object writeReplace() throws ObjectStreamException{
		throw new UnsupportedOperationException(SERIALIZATION_INITIAL_TYPE_ERROR_MSG);
	}

	@Override
	public Collection<String> loadedAttributes() {
		return ((MetaAttributeValueProvider)getProvider()).getItemState().keySet();
	}

	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public boolean isExist() {
		return true;
	}

	@Override
	public <T> T getOriginValue(String attributeName) {
		return getValue(attributeName);
	}

	@Override
	public <T> T getOriginLocalizedValue(String attributeName) {
		return getLocalizedValue(attributeName);
	}

	@Override
	public <T> T getOriginLocalizedValue(String attributeName, Locale locale) {
		return getLocalizedValue(attributeName, locale);
	}

	@Override
	public <T> Map<Locale, T> getOriginLocalizedValues(String attributeName) {
		return getLocalizedValues(attributeName);
	}

	@Override
	public <T> T getValue(String attributeName) {
		return getProvider().getValue(attributeName, this);
	}

	@Override
	public <T> T getLocalizedValue(String attributeName) {
		return null;
	}

	@Override
	public <T> Map<Locale, T> getLocalizedValues(String attributeName) {
		return null;
	}

	@Override
	public <T> T getLocalizedValue(String attributeName, Locale locale) {
		return null;
	}

	@Override
	public <T> void setValue(String attributeName, T value) {
		throw new UnsupportedOperationException(UPDATE_INITIAL_TYPE_ERROR_MSG);
	}

	@Override
	public <T> void setLocalizedValue(String attributeName, T value) {
		throw new UnsupportedOperationException(UPDATE_INITIAL_TYPE_ERROR_MSG);
	}

	@Override
	public <T> void setLocalizedValue(String attributeName, T value, Locale locale) {
		throw new UnsupportedOperationException(UPDATE_INITIAL_TYPE_ERROR_MSG);
	}

	@Override
	public void initValue(String attributeName, Object value) {
		throw new UnsupportedOperationException(UPDATE_INITIAL_TYPE_ERROR_MSG);
	}

	@Override
	public void flush() {
		throw new UnsupportedOperationException(UPDATE_INITIAL_TYPE_ERROR_MSG);
	}

	@Override
	public void refresh() {
		throw new UnsupportedOperationException(UPDATE_INITIAL_TYPE_ERROR_MSG);
	}

	@Override
	public boolean isSystemType() {
		return true;
	}

	@Override
	public MetaItemContext clone()  {
		return this;//it is necessary only for MetaItemContext
	}
}
