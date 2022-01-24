package com.coretex.orm.core.activeorm.query.operations.dataholders;

import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class QueryInfoHolder {

	private Logger LOG = LoggerFactory.getLogger(QueryInfoHolder.class);

	private final String query;
	private final Set<MetaTypeItem> itemsUsed = Sets.newHashSet();

	private String resultQuery;
	private boolean valid;
	private Throwable validationThrowable;
	private boolean localizedTable = false;

	public QueryInfoHolder(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public void addItemUsed(MetaTypeItem itemClass) {
		LOG.debug("Item :: [" + itemClass.getTypeCode() + ":" + itemClass.getUuid().toString() + "] used for query :: ["+query+"]");
		itemsUsed.add(itemClass);
	}

	public Set<MetaTypeItem> getItemsUsed() {
		return Set.copyOf(itemsUsed);
	}

	public void addAllItemUsed(Set<MetaTypeItem> metaTypes) {
		if (CollectionUtils.isNotEmpty(metaTypes)) {
			LOG.debug("Items :: [" + metaTypes.stream().map(metaTypeItem -> metaTypeItem.getTypeCode() + ":" + metaTypeItem.getUuid().toString()).collect(Collectors.joining(", ")) + "] used for query :: ["+query+"]");
			itemsUsed.addAll(metaTypes);
		}
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Throwable getValidationThrowable() {
		return validationThrowable;
	}

	public void setValidationThrowable(Throwable validationThrowable) {
		this.validationThrowable = validationThrowable;
	}

	public String getValidationMessage() {
		return validationThrowable.getMessage();
	}

	public String getResultQuery() {
		return resultQuery;
	}

	public void setResultQuery(String resultQuery) {
		this.resultQuery = resultQuery;
	}

	public boolean isLocalizedTable() {
		return localizedTable;
	}

	public void setLocalizedTable(boolean localizedTable) {
		this.localizedTable = localizedTable;
	}
}
