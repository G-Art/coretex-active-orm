package com.coretex.orm.core.activeorm.cache.impl;

import com.coretex.orm.core.activeorm.cache.CacheContext;
import com.coretex.orm.core.activeorm.cache.CacheInvalidationContext;
import com.coretex.orm.core.activeorm.cache.CacheManager;
import com.coretex.orm.core.activeorm.cache.CacheSegment;
import com.coretex.orm.core.activeorm.cache.CacheService;
import com.coretex.orm.core.activeorm.cache.SegmentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DefaultActiveSearchCacheService implements CacheService {

	private final Logger LOG = LoggerFactory.getLogger(DefaultActiveSearchCacheService.class);
	private final SegmentManager segmentManager;

	public DefaultActiveSearchCacheService(SegmentManager segmentManager) {
		this.segmentManager = segmentManager;
	}

	@Override
	public <R> R get(CacheContext cacheContext, Supplier<R> supplier) {
		CacheSegment<CacheInvalidationContext, CacheContext> segment = (CacheSegment<CacheInvalidationContext, CacheContext>) segmentManager.findSegment(cacheContext);

		if (Objects.isNull(segment)) {
			return supplier.get();
		}
		return segment.retrieveValue(() -> segment.apply(cacheManager -> {
			var key = segment.createKey(cacheContext);
			Callable<Object> callable = () -> segment.storeValue(supplier);
			return cacheManager.get(key, callable);
		}));

	}

	@Override
	public void invalidateFor(CacheInvalidationContext context) {
		segmentManager.findSegments(new SegmentManager.SegmentFilter() {
			@Override
			public Stream<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>> doFilter(Map<Class<? extends CacheContext>, Set<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>>> segments) {
				return segments
						.entrySet()
						.stream()
						.flatMap(entry -> entry.getValue()
								.stream())
						.filter(context::filter);
			}
		}).forEach(segment -> segment.accept(cacheManager -> {
			var affectedKeys = context.selectAllAffectedKeys(segment);
			cacheManager.invalidate(affectedKeys);
		}));
	}

	public void invalidateAll() {
		LOG.debug("Start all cache invalidation");
		segmentManager.allSegments()
				.forEach(segment -> segment.accept(CacheManager::invalidateAll));
		LOG.debug("Cache has bean invalidated");
	}
}
