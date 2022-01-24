package com.coretex.orm.core.activeorm.cache;

import java.util.Set;
import java.util.function.Predicate;

public interface CacheKeyComputationStrategy<F, CCTX extends CacheContext>{

	<K> K createKey(CCTX ctx);

	<K> Set<K> collectFeaturedKeys(Predicate<F> predicate);

	boolean containFeature(Predicate<F> predicate);
}
