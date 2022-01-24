package com.coretex.orm.core.activeorm.cache.features;

import com.coretex.orm.core.activeorm.cache.CacheContext;
import com.coretex.orm.core.activeorm.cache.CacheSegmentFeature;
import com.coretex.orm.core.activeorm.cache.impl.FeaturedStatementCacheContext;
import org.apache.commons.collections4.CollectionUtils;

public class GenericTypeCacheSegmentFeature implements CacheSegmentFeature {

	private int scoreBasis = 1;

	@Override
	public long getScore(CacheContext cacheContext) {
		if(cacheContext instanceof FeaturedStatementCacheContext) {
			var itemsUsed = ((FeaturedStatementCacheContext) cacheContext).getItemsUsed();
			if(CollectionUtils.isNotEmpty(itemsUsed)){
				return scoreBasis;
			}
		}

		return 0;
	}

	public int getScoreBasis() {
		return scoreBasis;
	}

	public void setScoreBasis(int scoreBasis) {
		this.scoreBasis = scoreBasis;
	}

}
