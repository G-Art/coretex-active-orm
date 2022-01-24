package com.coretex.orm.core.activeorm.cache;

import com.coretex.orm.core.activeorm.cache.impl.GuavaCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;

public abstract class CacheManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(GuavaCacheManager.class);
	private final CacheConfiguration cacheConfiguration;

	public CacheManager(CacheConfiguration cacheConfiguration) {
		this.cacheConfiguration = cacheConfiguration;
		init();
		LOGGER.info("Cache manager :: " + cacheType() + " :: [" + this.getClass().getName() + "] has bean configured");
		LOGGER.info("**************" + cacheType() + " Cache manager  ****************");
		LOGGER.info("*");
		LOGGER.info("*                      Configuration");
		LOGGER.info("*");
		LOGGER.info("* concurrencyLevel     :: " + cacheConfiguration.getConcurrencyLevel());
		LOGGER.info("* duration (seconds)   :: " + cacheConfiguration.getDuration().getSeconds());
		LOGGER.info("* maximumSize          :: " + cacheConfiguration.getMaximumSize());
		LOGGER.info("* ");
		LOGGER.info("*                      Initial Stats");
		LOGGER.info("*");
		LOGGER.info("* loadTime             :: " + stats().totalLoadTime());
		LOGGER.info("* loadCount            :: " + stats().loadCount());
		LOGGER.info("* loadSuccessCount     :: " + stats().loadSuccessCount());
		LOGGER.info("* loadExceptionCount   :: " + stats().loadExceptionCount());
		LOGGER.info("* loadExceptionRate    :: " + stats().loadExceptionRate());
		LOGGER.info("* averageLoadPenalty   :: " + stats().averageLoadPenalty());
		LOGGER.info("* hitCount             :: " + stats().hitCount());
		LOGGER.info("* hitRate              :: " + stats().hitRate());
		LOGGER.info("* missCount            :: " + stats().missCount());
		LOGGER.info("* missRate             :: " + stats().missRate());
		LOGGER.info("* requestCount         :: " + stats().requestCount());
		LOGGER.info("* ");
		LOGGER.info("**************" + cacheType() + " Cache manager ****************");
	}

	protected abstract String cacheType();

	public abstract void init();

	public CacheConfiguration getCacheConfiguration() {
		return cacheConfiguration;
	}

	public abstract <K, V> V get(K key, Callable<? extends V> loader);

	public abstract <K, V> void put(K key, Callable<? extends V> loader);

	public abstract <K, V> V getIfPresent(K key);

	public abstract Stats<?> stats();

	public abstract <K> void invalidate(K key);

	public abstract Map<Object, Object> toMap();

	public abstract <K> void invalidate(Iterable<K> keys);

	public abstract void invalidateAll();
}
