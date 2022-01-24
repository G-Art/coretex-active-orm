package com.coretex.orm.core.activeorm.cache.impl;

import com.coretex.orm.core.activeorm.cache.Stats;
import com.google.common.cache.CacheStats;

public class GuavaCacheStats extends Stats<CacheStats> {

	public GuavaCacheStats(CacheStats stats) {
		super(stats);
	}

	@Override
	public long requestCount() {
		return getStats().requestCount();
	}
	@Override
	public long hitCount() {
		return getStats().hitCount();
	}
	@Override
	public double hitRate() {
		return getStats().hitRate();
	}
	@Override
	public long missCount() {
		return getStats().missCount();
	}
	@Override
	public double missRate() {
		return getStats().missRate();
	}
	@Override
	public long loadCount() {
		return getStats().loadCount();
	}
	@Override
	public long loadSuccessCount() {
		return getStats().loadSuccessCount();
	}
	@Override
	public long loadExceptionCount() {
		return getStats().loadExceptionCount();
	}
	@Override
	public double loadExceptionRate() {
		return getStats().loadExceptionRate();
	}
	@Override
	public long totalLoadTime() {
		return getStats().totalLoadTime();
	}
	@Override
	public double averageLoadPenalty() {
		return getStats().averageLoadPenalty();
	}
	@Override
	public long evictionCount() {
		return getStats().evictionCount();
	}

}
