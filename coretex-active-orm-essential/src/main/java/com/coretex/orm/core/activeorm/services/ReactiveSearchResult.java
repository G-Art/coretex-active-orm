package com.coretex.orm.core.activeorm.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class ReactiveSearchResult<T> {

	private Logger LOG = LoggerFactory.getLogger(ReactiveSearchResult.class);

	private Stream<T> result;

	public ReactiveSearchResult(Supplier<Stream<T>> resultSupplier) {
		this.result = resultSupplier.get();
	}

	public Stream<T> getResultStream() {
		return result;
	}
}
