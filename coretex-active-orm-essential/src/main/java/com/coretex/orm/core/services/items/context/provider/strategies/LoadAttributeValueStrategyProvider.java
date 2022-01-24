package com.coretex.orm.core.services.items.context.provider.strategies;

import com.coretex.orm.core.services.items.exceptions.NotFoundAttributeLoaderException;
import com.coretex.items.core.MetaAttributeTypeItem;

import java.util.Map;

public class LoadAttributeValueStrategyProvider {

	private Map<String, LoadAttributeValueStrategy> loadAttributeValueStrategyMap;

	public LoadAttributeValueStrategy strategyForAttribute(MetaAttributeTypeItem metaAttributeTypeItem){

		return loadAttributeValueStrategyMap.computeIfAbsent(metaAttributeTypeItem.getAttributeTypeCode(), key -> {
			throw new NotFoundAttributeLoaderException(String.format("Unable to find value load strategy for [%s]", key));
		});

	}

	public void setLoadAttributeValueStrategyMap(Map<String, LoadAttributeValueStrategy> loadAttributeValueStrategyMap) {
		this.loadAttributeValueStrategyMap = loadAttributeValueStrategyMap;
	}
}
