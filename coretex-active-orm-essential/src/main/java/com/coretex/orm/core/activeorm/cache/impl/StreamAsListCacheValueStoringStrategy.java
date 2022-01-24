package com.coretex.orm.core.activeorm.cache.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StreamAsListCacheValueStoringStrategy<T> extends StreamAsCollectionCacheValueStoringStrategy<T, List<T>> {

	public StreamAsListCacheValueStoringStrategy() {
	}

	public StreamAsListCacheValueStoringStrategy(Function<T, T> outModification) {
		super(outModification);
	}

	@Override
	public List<T> createCollection() {
		return new ArrayList<>();
	}
}
