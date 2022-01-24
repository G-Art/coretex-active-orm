package com.coretex.orm.core.services.items.context.attributes;

import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.context.provider.AttributeProvider;
import com.coretex.orm.meta.AbstractGenericItem;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class AttributeValueHolder implements AttributeValueHolderState {

	public static AttributeValueHolder createLazyValueHolder(String attributeName, ItemContext owner) {
		return new AttributeValueHolder(attributeName, owner);
	}

	public static AttributeValueHolder initValueHolder(String attributeName, ItemContext owner, Object initialValue) {
		AttributeValueHolder valueHolder = createLazyValueHolder(attributeName, owner);
		valueHolder.originalValue = initialValue;
		valueHolder.value = initialValue;
		valueHolder.loaded = true;
		return valueHolder;
	}

	private final String attributeName;

	private final ItemContext owner;

	private Object originalValue;

	private Object value;

	private volatile boolean dirty = false;

	private volatile boolean loaded = false;

	private AttributeValueHolder(String attributeName, ItemContext owner) {
		checkArgument(isNotBlank(attributeName), "Attribute name is required to construct value holder");
		checkArgument(nonNull(owner), "Item context is required to construct value holder");
		this.attributeName = attributeName;
		this.owner = owner;
	}

	public Object get(AttributeProvider provider) {
		if (loaded) {
			return value;
		}

		originalValue = provider.getValue(attributeName, owner);
		if(originalValue instanceof Collection){
			var collection = originalValue instanceof Set ? new ObservedHashSet() : new ObservedArrayList();
			collection.addAll((Collection<?>) originalValue);
			value = collection;
		}else{
			value = originalValue;
		}

		loaded = true;
		return value;
	}

	public Object getOriginalValue(AttributeProvider provider){
		get(provider);
		return originalValue;
	}

	public void set(Object newValue) {
		if(newValue instanceof Collection){
			var collection = newValue instanceof Set ? new ObservedHashSet() : new ObservedArrayList();
			collection.addAll((Collection<?>) newValue);
			value = collection;
		}else{
			value = newValue;
		}
		dirty = isLoaded();
	}


	@Override
	public void flush() {
		if(value instanceof Collection){
			var collection = value instanceof Set ? new ObservedHashSet() : new ObservedArrayList();
			collection.addAll((Collection<?>) value);
			originalValue = collection;
		}else{
			originalValue = value;
		}
		dirty = false;
	}

	@Override
	public boolean isDirty() {
		boolean collectionDirty = false;
		if(Objects.nonNull(originalValue) && originalValue instanceof Collection){
			collectionDirty = ((Collection<?>) originalValue).stream()
					.anyMatch(o -> o instanceof AbstractGenericItem && ((AbstractGenericItem) o).getItemContext().isDirty());
		}
		return owner.isNew() || dirty || collectionDirty;
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

	public String getAttributeName() {
		return attributeName;
	}

	protected class ObservedArrayList extends ForwardingList<Object> {
		final List<Object> delegate = Lists.newArrayList(); // backing list
		@Override protected List<Object> delegate() {
			return delegate;
		}
		@Override public void add(int index, Object elem) {
			dirty = true;
			super.add(index, elem);
		}
		@Override public boolean add(Object elem) {
			dirty = true;
			return standardAdd(elem);
		}
		@Override public boolean addAll(Collection<?> c) {
			dirty = true;
			return standardAddAll(c);
		}
	}

	protected class ObservedHashSet extends ForwardingSet<Object> {
		final Set<Object> delegate = Sets.newHashSet(); // backing list
		@Override protected Set<Object> delegate() {
			return delegate;
		}
		@Override public boolean add(Object elem) {
			dirty = true;
			return super.add(elem); // implements in terms of add(int, E)
		}
		@Override public boolean addAll(Collection<?> c) {
			dirty = true;
			return standardAddAll(c);
		}
	}

}
