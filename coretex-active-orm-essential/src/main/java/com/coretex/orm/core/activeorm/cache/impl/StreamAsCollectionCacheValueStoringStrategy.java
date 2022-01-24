package com.coretex.orm.core.activeorm.cache.impl;

import com.coretex.orm.core.activeorm.cache.CacheValueRetrievingStrategy;
import com.coretex.orm.core.activeorm.cache.CacheValueStoringStrategy;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class StreamAsCollectionCacheValueStoringStrategy<T, C extends Collection<T>> implements CacheValueStoringStrategy<Stream<T>, C>, CacheValueRetrievingStrategy<Stream<T>, C> {

	private Function<T, T> outModification = t -> {
		if (t instanceof Cloneable) {
			return ObjectUtils.cloneIfPossible(t);
		}
		return t;
	};

	public StreamAsCollectionCacheValueStoringStrategy() {
	}

	public StreamAsCollectionCacheValueStoringStrategy(Function<T, T> outModification) {
		this.outModification = outModification;
	}

	@Override
	public C storeIncome(Stream<T> stream) {
		return stream.collect(Collectors.<T, C>toCollection(this::createCollection));
	}

	@Override
	public Stream<T> storeOutcome(C value) {
		return value.stream()
				.map(outModification);
	}

	public abstract C createCollection();
}
