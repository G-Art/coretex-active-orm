package com.coretex.orm.core.activeorm.cache.impl;

import com.coretex.orm.core.activeorm.cache.CacheContext;
import com.coretex.orm.core.activeorm.cache.CacheInvalidationContext;
import com.coretex.orm.core.activeorm.cache.CacheSegment;
import com.coretex.items.core.GenericItem;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

public class ItemBasedCacheInvalidationContext implements CacheInvalidationContext {

	private final GenericItem item;

	private final Predicate<?> filter;

	public ItemBasedCacheInvalidationContext(GenericItem item) {
		Objects.requireNonNull(item);
		this.item = item;
		this.filter = type -> type.equals(item.getMetaType());
	}

	@Override
	public boolean filter(CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext> segment) {
		return segment.hasAffectedKeys(this.filter);
	}

	@Override
	public <K> Collection<K> selectAllAffectedKeys(CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext> segment) {
		return segment.featuredKeys(this.filter);
	}
}
