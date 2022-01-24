package com.coretex.orm.core.services.items.context.attributes;

import com.coretex.orm.core.AbstractCoretexContextProvider;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.context.provider.AttributeProvider;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.LocaleUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class LocalizedAttributeValueHolder implements AttributeValueHolderState {

	public static LocalizedAttributeValueHolder createLazyValueHolder(String attributeName, ItemContext owner) {
		return new LocalizedAttributeValueHolder(attributeName, owner);
	}

	public static LocalizedAttributeValueHolder initValueHolder(String attributeName, ItemContext owner, Map<String, Object>  initialValue) {
		LocalizedAttributeValueHolder valueHolder = createLazyValueHolder(attributeName, owner);
		valueHolder.originalValue = Optional.ofNullable(initialValue).orElseGet(Maps::newHashMap);
		valueHolder.value = Optional.ofNullable(initialValue).orElseGet(Maps::newHashMap);
		valueHolder.loaded = true;
		return valueHolder;
	}

	private final String attributeName;

	private final ItemContext owner;

	private Map<String, Object> originalValue;

	private Map<String, Object> value;

	private Locale defaultLocale;

	private volatile boolean dirty = false;

	private volatile boolean loaded = false;

	private LocalizedAttributeValueHolder(String attributeName, ItemContext owner) {
		checkArgument(isNotBlank(attributeName), "Attribute name is required to construct value holder");
		checkArgument(nonNull(owner), "Item context is required to construct value holder");
		AbstractCoretexContextProvider.getInstance().getMetaAttributeTypeValidator().validate(attributeName, owner);
//		metaTypeProvider = ApplicationContextProvider.getBean(MetaTypeProvider.class);
//		MetaAttributeTypeItem metaAttributeTypeItem = metaTypeProvider.findAttribute(owner.getTypeCode(), attributeName);
//		checkNotNull(metaAttributeTypeItem, "Attribute ["+attributeName+"] is not exist for type ["+owner.getTypeCode()+"]");
//		Preconditions.checkArgument(metaAttributeTypeItem.getLocalized(), "Attribute ["+attributeName+"] is not localized for type ["+owner.getTypeCode()+"]");

		defaultLocale = Locale.ENGLISH;//TODO make it configurable
		this.attributeName = attributeName;
		this.owner = owner;
	}

	public Object get(AttributeProvider provider, Locale locale) {
		if (loaded) {
			return value.get(Optional.ofNullable(locale).orElse(defaultLocale).toString());
		}

		loadLocalizedValue(provider);
		return value.get(Optional.ofNullable(locale).orElse(defaultLocale).toString());
	}

	public Map<Locale, Object> getAll(AttributeProvider provider){
		if (loaded) {
			return transform(value);
		}

		loadLocalizedValue(provider);
		return transform(value);
	}

	public Object getOriginalValue(AttributeProvider provider, Locale locale) {
		if (!loaded) {
			loadLocalizedValue(provider);
		}
		return originalValue.get(Optional.ofNullable(locale).orElse(defaultLocale).toString());
	}

	public Object getOriginalValue(AttributeProvider provider) {
		return getOriginalValue(provider, null);
	}

	public Map<Locale, Object> getOriginalValues(AttributeProvider provider) {
		if (!loaded) {
			loadLocalizedValue(provider);
		}
		return transform(originalValue);
	}

	public void set(Object newValue, Locale locale) {
		value.put(Optional.ofNullable(locale).orElse(defaultLocale).toString(), newValue);
		dirty = isLoaded();
	}

	public void setAll(Map<String, Object> newValues) {
		value.putAll(newValues);
		dirty = isLoaded();
	}

	@Override
	public void flush() {
		originalValue.putAll(value);
		dirty = false;
	}

	@Override
	public boolean isDirty() {
		return owner.isNew() || dirty;
	}

	@Override
	public boolean isLoaded() {
		return owner.isNew() || loaded;
	}

	@Override
	public void refresh() {
		value = null;
		originalValue = null;
		loaded = false;
		dirty = false;
	}

	private void loadLocalizedValue(AttributeProvider provider) {
		originalValue = provider.getValue(attributeName, owner);
		if(value != null && !value.isEmpty()){
			originalValue.forEach(value::putIfAbsent);
		}else {
			value = originalValue;
		}

		loaded = true;
	}

	private Map<Locale, Object> transform(Map<String, Object> value) {
		Map<Locale, Object> result = new HashMap<>();
		value.forEach((key, val) -> result.put(LocaleUtils.toLocale(key), val));
		return result.isEmpty() ? null : result;
	}

	public String getAttributeName() {
		return attributeName;
	}
}
