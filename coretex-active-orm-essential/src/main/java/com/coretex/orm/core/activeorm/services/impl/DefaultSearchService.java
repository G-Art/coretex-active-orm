package com.coretex.orm.core.activeorm.services.impl;

import com.coretex.orm.core.activeorm.query.specs.select.PageableSelectOperationSpec;
import com.coretex.orm.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.orm.core.activeorm.services.AbstractJdbcService;
import com.coretex.orm.core.activeorm.services.PageableSearchResult;
import com.coretex.orm.core.activeorm.services.SearchResult;
import com.coretex.orm.core.activeorm.services.SearchService;

import java.util.Map;


public class DefaultSearchService extends AbstractJdbcService implements SearchService {

	@Override
	public <T> SearchResult<T> search(String query) {
		SelectOperationSpec selectOperationSpec = new SelectOperationSpec(query);
		return search(selectOperationSpec);
	}

	@Override
	public <T> SearchResult<T> search(String query, Map<String, Object> parameters) {
		SelectOperationSpec selectOperationSpec = new SelectOperationSpec(query, parameters);
		return search(selectOperationSpec);
	}

	@Override
	public <T> SearchResult<T> search(SelectOperationSpec spec) {
		return getOrmOperationExecutor().execute(spec.createOperationContext());
	}

	@Override
	public <T> PageableSearchResult<T> searchPageable(String query) {
		var selectOperationSpec = new PageableSelectOperationSpec(query);
		return searchPageable(selectOperationSpec);
	}

	@Override
	public <T> PageableSearchResult<T> searchPageable(String query, long count) {
		var selectOperationSpec = new PageableSelectOperationSpec(query);
		selectOperationSpec.setCount(count);
		return searchPageable(selectOperationSpec);
	}

	@Override
	public <T> PageableSearchResult<T> searchPageable(String query, long count, long page) {
		var selectOperationSpec = new PageableSelectOperationSpec(query);
		selectOperationSpec.setCount(count);
		selectOperationSpec.setPage(page);
		return searchPageable(selectOperationSpec);
	}

	@Override
	public <T> PageableSearchResult<T> searchPageable(String query, Map<String, Object> parameters, long count, long page) {
		var selectOperationSpec = new PageableSelectOperationSpec(query, parameters);
		selectOperationSpec.setCount(count);
		selectOperationSpec.setPage(page);
		return searchPageable(selectOperationSpec);
	}

	@Override
	public <T> PageableSearchResult<T> searchPageable(String query, Map<String, Object> parameters) {
		var selectOperationSpec = new PageableSelectOperationSpec(query, parameters);
		return searchPageable(selectOperationSpec);
	}

	@Override
	public <T> PageableSearchResult<T> searchPageable(PageableSelectOperationSpec spec) {
		return getOrmOperationExecutor().execute(spec.createOperationContext());
	}

}
