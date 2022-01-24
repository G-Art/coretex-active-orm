package com.coretex.orm.core.activeorm.cache.impl;

import com.coretex.orm.core.activeorm.cache.CacheContext;
import com.coretex.orm.core.activeorm.cache.CacheInvalidationContext;
import com.coretex.orm.core.activeorm.cache.CacheSegment;
import com.coretex.orm.core.activeorm.cache.SegmentManager;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultSegmentManager implements SegmentManager {

	private final Logger LOG = LoggerFactory.getLogger(DefaultSegmentManager.class);

	private final Map<Class<? extends CacheContext>, Set<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>>> segments;
	private final Set<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>> allSegments;

	public DefaultSegmentManager(Set<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>> segments) {
		this.allSegments = segments;
		this.segments = segments.stream()
				.collect(Collectors.toMap(CacheSegment::getCtxClass,
						Sets::newHashSet,
						(ol, or) -> {
							ol.addAll(or);
							return ol;
						}));
	}

	@Override
	public CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext> findSegment(CacheContext cacheContext) {

		Stream<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>> fSegment = findSegments(new SegmentFilter() {
			@Override
			public Stream<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>> doFilter(Map<Class<? extends CacheContext>, Set<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>>> segments) {
				return segments.entrySet()
						.stream()
						.flatMap(classSetEntry -> classSetEntry.getValue()
								.stream()
								.filter(segment -> segment.getCtxClass() == cacheContext.getClass()));
			}
		});

		return fSegment.max(Comparator.comparingLong(segment -> calculateScore(segment, cacheContext))).orElse(null);

	}

	@Override
	public Stream<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>> findSegments(SegmentFilter segmentFilter) {
		return segmentFilter.doFilter(segments);
	}

	@Override
	public Set<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>> allSegments() {
		return allSegments;
	}

	public long calculateScore(CacheSegment<?, ?> segment, CacheContext cacheContext) {
		return segment.featuresList()
				.stream()
				.map(feature -> feature.getScore(cacheContext))
				.mapToLong(Long::longValue)
				.sum();
	}


}
