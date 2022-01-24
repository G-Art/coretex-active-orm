package com.coretex.orm.core.activeorm.cache;

public abstract class Stats<S> {

	private final S stats;

	public Stats(S stats) {
		this.stats = stats;
	}


	public S getStats() {
		return stats;
	}

	public abstract long requestCount();

	public abstract long hitCount();

	public abstract double hitRate();

	public abstract long missCount();

	public abstract double missRate();

	public abstract long loadCount();

	public abstract long loadSuccessCount();

	public abstract long loadExceptionCount();

	public abstract double loadExceptionRate();

	public abstract long totalLoadTime();

	public abstract double averageLoadPenalty();

	public abstract long evictionCount();
}
