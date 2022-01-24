package com.coretex.orm.core.activeorm.cache;

import java.util.Collection;

public interface CacheInvalidationContext {
	boolean filter(CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext> segment);

	<K> Collection<K> selectAllAffectedKeys(CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext> segment);
}
