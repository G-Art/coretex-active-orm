package com.coretex.orm.core.activeorm.services;

import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchResult<T> extends ReactiveSearchResult<T> {

	private Logger LOG = LoggerFactory.getLogger(SearchResult.class);

	private List<T> result;
	private int count;

	public SearchResult(Supplier<Stream<T>> resultSupplier) {
		super(resultSupplier);
		List<T> result = Collections.emptyList();
		try {
			result = getResultStream().collect(Collectors.toList());
		} catch (Exception e) {
			LOG.error("No result by search was supplied", e);
		}
		this.result = CollectionUtils.isNotEmpty(result) ? ImmutableList.copyOf(result) : Collections.emptyList();
		this.count = this.result.size();
	}

	public List<T> getResult() {
		return result;
	}

	public int getCount() {
		return count;
	}

	@Override
	public Stream<T> getResultStream() {
		if (Objects.nonNull(this.result)) {
			return this.result.stream();
		}
		return super.getResultStream();
	}
}
