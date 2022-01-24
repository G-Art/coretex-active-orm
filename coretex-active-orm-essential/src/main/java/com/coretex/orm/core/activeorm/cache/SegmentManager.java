package com.coretex.orm.core.activeorm.cache;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public interface SegmentManager {

	CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext> findSegment(CacheContext cacheContext);

	Stream<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>> findSegments(SegmentFilter segmentFilter);

	Set<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>> allSegments();

	abstract class SegmentFilter {
		public abstract Stream<CacheSegment<? extends CacheInvalidationContext, ? extends CacheContext>> doFilter(Map<Class<? extends CacheContext>, Set<CacheSegment<? extends CacheInvalidationContext,? extends CacheContext>>> segments);
	}
}
