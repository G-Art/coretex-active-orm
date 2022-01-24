package com.coretex.orm.core.activeorm.cache.impl;

import com.coretex.orm.core.activeorm.cache.CacheContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.items.core.MetaTypeItem;

import java.util.Map;
import java.util.Set;

public class FeaturedStatementCacheContext implements CacheContext {

	private final QueryInfoHolder queryInfoHolder;
	private Map<String, Object> parameters;

	public FeaturedStatementCacheContext(QueryInfoHolder queryInfoHolder, Map<String, Object> parameters) {
		this.queryInfoHolder = queryInfoHolder;
		this.parameters = parameters;
	}

	public String getResultQuery() {
		return queryInfoHolder.getResultQuery();
	}

	public boolean isLocalized() {
		return queryInfoHolder.isLocalizedTable();
	}

	public Set<MetaTypeItem> getItemsUsed() {
		return queryInfoHolder.getItemsUsed();
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
}