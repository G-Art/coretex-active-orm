package com.coretex.orm.core.activeorm.cache.impl;

import com.coretex.orm.core.activeorm.cache.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class DefaultCacheSegment<CICTX extends CacheInvalidationContext, CCTX extends CacheContext> implements CacheSegment<CICTX ,CCTX> {

	private final CacheKeyComputationStrategy<?, CCTX> cacheKeyComputationStrategy;
	private final CacheValueStoringStrategy cacheValueStoringStrategy;
	private final CacheValueRetrievingStrategy cacheValueRetrievingStrategy;

	private final Class<CICTX> invalidationCtxClass;
	private final Class<CCTX> ctxClass;
	private final CacheManager cacheManager;
	private final List<CacheSegmentFeature> features;
	private String cacheSegmentName;

	public DefaultCacheSegment(CacheKeyComputationStrategy<?, CCTX> cacheKeyComputationStrategy,
	                           CacheValueStoringStrategy cacheValueStoringStrategy,
	                           CacheValueRetrievingStrategy cacheValueRetrievingStrategy,
	                           Class<CCTX> ctxClass, Class<CICTX> invalidationCtxClass,
	                           CacheManager cacheManager, List<CacheSegmentFeature> features) {
		Objects.requireNonNull(cacheManager);
		Objects.requireNonNull(cacheValueStoringStrategy);
		Objects.requireNonNull(cacheKeyComputationStrategy);
		Objects.requireNonNull(ctxClass);
		Objects.requireNonNull(invalidationCtxClass);
		Objects.requireNonNull(features);
		Objects.requireNonNull(cacheManager);

		this.cacheValueStoringStrategy = cacheValueStoringStrategy;
		this.cacheValueRetrievingStrategy = cacheValueRetrievingStrategy;
		this.cacheKeyComputationStrategy = cacheKeyComputationStrategy;
		this.ctxClass = ctxClass;
		this.invalidationCtxClass = invalidationCtxClass;
		this.features = features;
		this.cacheManager = cacheManager;
	}

	@Override
	public <R> R apply(Function<CacheManager, R> function) {
		return function.apply(cacheManager);
	}

	@Override
	public void accept(Consumer<CacheManager> consumer) {
		consumer.accept(cacheManager);
	}

	@Override
	public boolean hasAffectedKeys(Predicate predicate){
		return cacheKeyComputationStrategy.containFeature(predicate);
	}

	@Override
	public <K> Set<K> featuredKeys(Predicate predicate){
		return cacheKeyComputationStrategy.collectFeaturedKeys(predicate);
	}

	@Override
	public <K> K createKey(CCTX cacheContext) {
		return (K) cacheKeyComputationStrategy.createKey(cacheContext);
	}

	@Override
	public <R> R storeValue(Supplier<?> supplier){
		return (R) cacheValueStoringStrategy.storeIncome(supplier.get());
	}

	@Override
	public <R> R retrieveValue(Supplier<?> supplier) {
		return (R) cacheValueRetrievingStrategy.storeOutcome(supplier.get());
	}

	@Override
	public List<CacheSegmentFeature> featuresList() {
		return features;
	}

	@Override
	public void setBeanName(String name) {
		this.cacheSegmentName = name;
	}

	@Override
	public Class<CCTX> getCtxClass() {
		return ctxClass;
	}

	@Override
	public Class<CICTX> getInvalidationCtxClass() {
		return invalidationCtxClass;
	}

	@Override
	public String getCacheSegmentName() {
		return cacheSegmentName;
	}
}
