package com.coretex.orm.core.activeorm.cache;

import org.springframework.beans.factory.BeanNameAware;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface CacheSegment<CICTX extends CacheInvalidationContext, CCTX extends CacheContext> extends BeanNameAware {

	<R> R storeValue(Supplier<?> supplier);

	<R> R retrieveValue(Supplier<?> supplier);

	List<CacheSegmentFeature> featuresList();

	boolean hasAffectedKeys(Predicate predicate);

	<K> Set<K> featuredKeys(Predicate predicate);

	<K> K createKey(CCTX cacheContext);

	<R> R apply(Function<CacheManager, R> function);

	void accept(Consumer<CacheManager> consumer);

	Class<CCTX> getCtxClass();

	Class<CICTX> getInvalidationCtxClass();

	String getCacheSegmentName();
}
