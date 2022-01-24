package com.coretex.orm.core.activeorm.cache.features;

import com.coretex.orm.core.activeorm.cache.CacheContext;
import com.coretex.orm.core.activeorm.cache.CacheSegmentFeature;
import com.coretex.orm.core.activeorm.cache.impl.FeaturedStatementCacheContext;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.items.core.MetaTypeItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static java.util.Objects.nonNull;

public class SingleItemCacheSegmentFeature implements CacheSegmentFeature {

	private static final Logger LOGGER = LoggerFactory.getLogger(SingleItemCacheSegmentFeature.class);

	private CoretexContext coretexContext;

	private int scoreBasis = 3;

	private int typePrecisionMultiplier = 100;
	private int minTypePrecisionMultiplier = 10;
	private int subTypePrecisionSubtractionValue = 10;

	private String type;
	private MetaTypeItem metaTypeItem;
	private boolean subTypeInclude = true;

	public SingleItemCacheSegmentFeature() {
	}

	public SingleItemCacheSegmentFeature(String type, CoretexContext coretexContext) {
		this.type = type;
		this.coretexContext = coretexContext;
		this.metaTypeItem = this.coretexContext.findMetaType(type);
	}

	@Override
	public long getScore(CacheContext cacheContext) {
		if (cacheContext instanceof FeaturedStatementCacheContext) {
			var itemsUsed = ((FeaturedStatementCacheContext) cacheContext).getItemsUsed();
			if (CollectionUtils.isNotEmpty(itemsUsed) && itemsUsed.size() == 1) {
				if (nonNull(type)) {
					var usedTypeItem = IterableUtils.first(itemsUsed);
					if (metaTypeItem.getTypeCode().equals(type)) {
						return scoreBasis * typePrecisionMultiplier;
					} else {
						if (subTypeInclude && CollectionUtils.isNotEmpty(metaTypeItem.getSubtypes())) {
							return scoreBasis * subTypeIncludeRate(usedTypeItem, typePrecisionMultiplier);
						}
					}
				} else {
					LOGGER.debug("Type is not specified");
				}
				return scoreBasis;
			}
		}

		return 0L;
	}

	private long subTypeIncludeRate(MetaTypeItem metaTypeItem, int typePrecisionMultiplier) {
		var subtypes = metaTypeItem.getSubtypes();

		return subTypeIncludeRate(subtypes, metaTypeItem, typePrecisionMultiplier);
	}

	private long subTypeIncludeRate(Set<MetaTypeItem> subtypes, MetaTypeItem metaTypeItem, int typePrecisionMultiplier) {
		if(subtypes.stream().anyMatch(subItem -> subItem.equals(metaTypeItem))){
			var newMultiplier = typePrecisionMultiplier - subTypePrecisionSubtractionValue;
			return Math.max(newMultiplier, minTypePrecisionMultiplier);
		}else {
			var result = 0L;
			for (MetaTypeItem mType : subtypes) {
				if (CollectionUtils.isNotEmpty(mType.getSubtypes())) {
					var subRes = subTypeIncludeRate(mType.getSubtypes(), metaTypeItem, typePrecisionMultiplier - subTypePrecisionSubtractionValue);
					result = Math.max(subRes, result);
				}
			}
			return result;
		}
	}

	public void setScoreBasis(int scoreBasis) {
		this.scoreBasis = scoreBasis;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSubTypeInclude(boolean subTypeInclude) {
		this.subTypeInclude = subTypeInclude;
	}

	public void setTypePrecisionMultiplier(int typePrecisionMultiplier) {
		this.typePrecisionMultiplier = typePrecisionMultiplier;
	}

	public void setMinTypePrecisionMultiplier(int minTypePrecisionMultiplier) {
		this.minTypePrecisionMultiplier = minTypePrecisionMultiplier;
	}

	public void setSubTypePrecisionSubtractionValue(int subTypePrecisionSubtractionValue) {
		this.subTypePrecisionSubtractionValue = subTypePrecisionSubtractionValue;
	}
}
