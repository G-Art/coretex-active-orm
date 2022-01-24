package com.coretex.orm.core.activeorm.cache.features;

import com.coretex.orm.core.activeorm.cache.CacheContext;
import com.coretex.orm.core.activeorm.cache.impl.FeaturedStatementCacheContext;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;

public class ItemTypeLocalizedCacheSegmentFeature extends ItemTypeCacheSegmentFeature {

	public ItemTypeLocalizedCacheSegmentFeature(String type, CoretexContext coretexContext) {
		super(type, coretexContext);
	}

	public ItemTypeLocalizedCacheSegmentFeature(String type, CoretexContext coretexContext, boolean useSubTypes) {
		super(type, coretexContext, useSubTypes);
	}

	@Override
	public long getScore(CacheContext cacheContext) {


		if (cacheContext instanceof FeaturedStatementCacheContext && ((FeaturedStatementCacheContext) cacheContext).isLocalized()) {
			return super.getScore(cacheContext) * 2;
		}

		return 0;
	}

}
