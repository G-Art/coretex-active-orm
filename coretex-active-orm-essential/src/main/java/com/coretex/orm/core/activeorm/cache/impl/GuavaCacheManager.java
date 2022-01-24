package com.coretex.orm.core.activeorm.cache.impl;

import com.coretex.orm.core.activeorm.cache.CacheConfiguration;
import com.coretex.orm.core.activeorm.cache.CacheManager;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class GuavaCacheManager extends CacheManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(GuavaCacheManager.class);

	private Cache<Object, Object> cache;

	public GuavaCacheManager(CacheConfiguration cacheConfiguration) {
		super(cacheConfiguration);
	}

	@Override
	protected String cacheType() {
		return "Guava";
	}

	@Override
	public void init() {
		var cacheBuilder = CacheBuilder.newBuilder();

		if (getCacheConfiguration().isSoftValue()) {
			cacheBuilder.softValues();
		}
		cacheBuilder.maximumSize(getCacheConfiguration().getMaximumSize());
		cacheBuilder.concurrencyLevel(getCacheConfiguration().getConcurrencyLevel());
		cacheBuilder.expireAfterAccess(getCacheConfiguration().getDuration());

		cache = cacheBuilder.build();

		LOGGER.info(String.format("Cache initialized, details: [%s]", cacheBuilder.toString()));
	}

	@Override
	public <K, V> V get(K key, Callable<? extends V> loader) {
		try {
			return (V) cache.get(key, loader);
		} catch (ExecutionException e) {
			Throwables.throwIfUnchecked(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public <K, V> void put(K key, Callable<? extends V> loader) {
		try {
			cache.put(key, loader.call());
		} catch (Exception e) {
			Throwables.throwIfUnchecked(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public <K, V> V getIfPresent(K key) {
		return (V) cache.getIfPresent(key);
	}

	@Override
	public GuavaCacheStats stats() {
		return new GuavaCacheStats(cache.stats());
	}

	@Override
	public <K> void invalidate(K key) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Cache invalidation request by key [%s]", key));
		}
		cache.invalidate(key);
	}

	@Override
	public Map<Object, Object> toMap() {
		return cache.asMap();
	}

	@Override
	public <K> void invalidate(Iterable<K> keys) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Cache invalidation request by keys [%s]", keys));
		}
		cache.invalidateAll(keys);
	}

	@Override
	public void invalidateAll() {
		cache.invalidateAll();
	}
}
