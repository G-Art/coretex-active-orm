package com.coretex.orm.core.activeorm.cache;

import java.util.function.Supplier;

public interface CacheService {

	<R> R get(CacheContext context, Supplier<R> supplier);

	void invalidateFor(CacheInvalidationContext context);
}
