package com.coretex.orm.core.activeorm.cache;

public interface CacheValueStoringStrategy<V, R> {
	R storeIncome(V value);

}
