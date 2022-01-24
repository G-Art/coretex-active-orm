package com.coretex.orm.core.activeorm.dao;

import com.coretex.orm.core.activeorm.dao.SortParameters.SortOrder;
import com.coretex.orm.core.activeorm.exceptions.AmbiguousResultException;
import com.coretex.orm.core.activeorm.query.specs.select.PageableSelectOperationSpec;
import com.coretex.orm.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.orm.core.activeorm.services.ItemService;
import com.coretex.orm.core.activeorm.services.PageableSearchResult;
import com.coretex.orm.core.activeorm.services.SearchResult;
import com.coretex.orm.core.activeorm.services.SearchService;
import com.coretex.items.core.GenericItem;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultGenericDao<I extends GenericItem> implements Dao<I> {

	private final String typecode;

	private SearchService searchService;

	private ItemService itemService;

	public DefaultGenericDao(String typecode ) {
		this.typecode = typecode;
	}

	@Override
	public void save(I item) {
		itemService.save(item);
	}

	@Override
	public void delete(I item) {
		itemService.delete(item);
	}

	@Override
	public Long count() {
		return count(false);
	}

	@Override
	public Long count(boolean strict) {
		SelectOperationSpec query = this.createCountSearchQuery(strict);
		var result = this.getSearchService().<Map<String, Long>>search(query).getResult();
		if (CollectionUtils.isEmpty(result)) {
			return 0L;
		}
		var resultMap = result.iterator().next();
		if (!resultMap.containsKey("count")) {
			return 0L;
		}
		return result.iterator().next().get("count");
	}

	@Override
	public List<I> find() {
		return find(false);
	}
	@Override
	public List<I> find(boolean strict) {
		return findReactive(strict).collect(Collectors.toList());
	}

	@Override
	public Stream<I> findReactive() {
		return findReactive(false);
	}

	@Override
	public Stream<I> findReactive(boolean strict) {
		SelectOperationSpec query = this.createSearchQuery(strict);
		return this.getSearchService().<I>search(query).getResultStream();
	}

	@Override
	public I find(UUID uuid) {
		return find(uuid, false);
	}

	@Override
	public I find(UUID uuid, boolean strict) {
		return findSingle(Map.of("uuid", uuid), false, strict);
	}

	public <R> SearchResult<R> findByQuery(String query, Map<String, Object> params){
		 return this.getSearchService().search(query, params);
	}
	public <R> SearchResult<R> findByQuery(String query){
		return this.getSearchService().search(query);
	}

	@Override
	public I findSingle(Map<String, ?> params, boolean throwAmbiguousException) {
		return findSingle(params, throwAmbiguousException, false);
	}
	@Override
	public I findSingle(Map<String, ?> params, boolean throwAmbiguousException, boolean strict) {
		var result = find(params, strict);
		if (CollectionUtils.isNotEmpty(result) && result.size() > 1 && throwAmbiguousException) {
			throw new AmbiguousResultException(String.format("Result contain more than one result (result count: %s)", result.size()));
		}
		return CollectionUtils.isNotEmpty(result) ? result.iterator().next() : null;
	}

	public  <R> R findSingleByQuery(String query, Map<String, Object> params){
		var searchResult = findByQuery(query, params);
		var result = searchResult.getResult();

		if (CollectionUtils.isNotEmpty(result) && result.size() > 1 ) {
			throw new AmbiguousResultException(String.format("Result contain more than one result (result count: %s)", result.size()));
		}
		if(result.isEmpty()){
			return null;
		}else{
			return (R) result.get(0);
		}
	}
	public <R> R findSingleByQuery(String query){
		var searchResult = findByQuery(query);
		var result = searchResult.getResult();

		if (CollectionUtils.isNotEmpty(result) && result.size() > 1 ) {
			throw new AmbiguousResultException(String.format("Result contain more than one result (result count: %s)", result.size()));
		}
		if(result.isEmpty()){
			return null;
		}else{
			return (R) result.get(0);
		}
	}

	@Override
	public Optional<I> findOne(Map<String, ?> params, boolean throwAmbiguousException) {
		return Optional.ofNullable(findSingle(params, throwAmbiguousException));
	}
	@Override
	public Optional<I> findOne(Map<String, ?> params, boolean throwAmbiguousException, boolean strict) {
		return Optional.ofNullable(findSingle(params, throwAmbiguousException, strict));
	}

	@Override
	public List<I> find(String query) {
		return findReactive(query).collect(Collectors.toList());
	}

	@Override
	public Stream<I> findReactive(String query) {
		SearchService ss = this.getSearchService();
		SearchResult<I> searchResult = ss.search(query);
		return searchResult.getResultStream();
	}

	@Override
	public List<I> find(String query, Map<String, Object> params) {
		return findReactive(query, params).collect(Collectors.toList());
	}

	@Override
	public Stream<I> findReactive(String query, Map<String, Object> params) {
		SearchService ss = this.getSearchService();
		SearchResult<I> searchResult = ss.search(query, params);
		return searchResult.getResultStream();
	}

	@Override
	public List<I> find(Map<String, ?> params) {
		return find(params, false);
	}

	@Override
	public List<I> find(Map<String, ?> params, boolean strict) {
		return findReactive(params, strict).collect(Collectors.toList());
	}

	@Override
	public Stream<I> findReactive(Map<String, ?> params) {
		return findReactive(params, false);
	}

	@Override
	public Stream<I> findReactive(Map<String, ?> params, boolean strict) {
		SelectOperationSpec query = this.createSearchQuery(params, strict);
		SearchResult<I> searchResult = this.getSearchService().search(query);
		return searchResult.getResultStream();
	}

	@Override
	public List<I> find(SortParameters sortParameters) {
		return find(sortParameters, false);
	}

	@Override
	public List<I> find(SortParameters sortParameters, boolean strict) {
		return findReactive(sortParameters, strict).collect(Collectors.toList());
	}

	@Override
	public Stream<I> findReactive(SortParameters sortParameters) {
		return findReactive(sortParameters, false);
	}

	@Override
	public Stream<I> findReactive(SortParameters sortParameters, boolean strict) {
		SelectOperationSpec query = this.createSearchQuery(sortParameters, strict);
		return this.getSearchService().<I>search(query).getResultStream();
	}

	@Override
	public List<I> find(Map<String, ?> params, SortParameters sortParameters) {
		return find(params, sortParameters, false);
	}

	@Override
	public List<I> find(Map<String, ?> params, SortParameters sortParameters, boolean strict) {
		return findReactive(params, sortParameters, strict).collect(Collectors.toList());
	}

	@Override
	public Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters) {
		return findReactive(params,sortParameters, false);
	}

	@Override
	public Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters, boolean strict) {
		var query = this.createSearchQuery(params, sortParameters, strict);
		return this.getSearchService().<I>search(query).getResultStream();
	}

	@Override
	public List<I> find(Map<String, ?> params, SortParameters sortParameters, long count) {
		return find(params, sortParameters, count, false);
	}

	@Override
	public List<I> find(Map<String, ?> params, SortParameters sortParameters, long count, boolean strict) {
		return findPageable(params, sortParameters, count, strict).getResult();
	}

	@Override
	public Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count) {
		return findReactive(params, sortParameters, count, false);
	}

	@Override
	public Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count, boolean strict) {
		return findPageable(params, sortParameters, count, strict).getResultStream();
	}

	@Override
	public List<I> find(Map<String, ?> params, SortParameters sortParameters, long count, long page) {
		return find(params, sortParameters, count, page, false);
	}

	@Override
	public List<I> find(Map<String, ?> params, SortParameters sortParameters, long count, long page, boolean strict) {
		return findPageable(params, sortParameters, count, page, strict).getResult();
	}

	@Override
	public Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count, long page) {
		return findReactive(params, sortParameters, count, page, false);
	}

	@Override
	public Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count, long page, boolean strict) {
		return findPageable(params, sortParameters, count, page, strict).getResultStream();
	}

	@Override
	public PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count) {
		return findPageable(params, sortParameters, count, false);
	}
	@Override
	public PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count, boolean strict) {
		var query = this.createSearchQuery(params, sortParameters, count, strict);
		return this.getSearchService().searchPageable(query);
	}

	@Override
	public PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count, long page) {
		return findPageable(params, sortParameters, count, page, false);
	}

	@Override
	public PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count, long page, boolean strict) {
		var query = this.createSearchQuery(params, sortParameters, count, page, strict);
		return this.getSearchService().searchPageable(query);
	}

	@Override
	public PageableSearchResult<I> findPageable(Map<String, ?> params, long count) {
		return findPageable(params,  count, false);
	}

	@Override
	public PageableSearchResult<I> findPageable(Map<String, ?> params, long count, boolean strict) {
		return findPageable(params, null, count, strict);
	}

	@Override
	public PageableSearchResult<I> findPageable(Map<String, ?> params, long count, long page) {
		return findPageable(params,  count, page, false);
	}

	@Override
	public PageableSearchResult<I> findPageable(Map<String, ?> params, long count, long page, boolean strict) {
		return findPageable(params, null, count, page, strict);
	}

	@Override
	public PageableSearchResult<I> findPageable(Map<String, ?> params) {
		return findPageable(params, false);
	}

	@Override
	public PageableSearchResult<I> findPageable(Map<String, ?> params, boolean strict) {
		return findPageable(params, null, -1, strict);
	}

	@Override
	public PageableSearchResult<I> findPageable(long count, long page) {
		return findPageable( count, page, false);
	}

	@Override
	public PageableSearchResult<I> findPageable(long count, long page, boolean strict) {
		return findPageable(null, count, page, strict);
	}

	@Override
	public PageableSearchResult<I> findPageable(long count) {
		return findPageable(count,false);
	}

	@Override
	public PageableSearchResult<I> findPageable(long count, boolean strict) {
		return findPageable(count, -1, strict);
	}

	@Override
	public PageableSearchResult<I> findPageable() {
		return findPageable(false);
	}

	@Override
	public PageableSearchResult<I> findPageable(boolean strict) {
		return findPageable(null, null, -1, strict);
	}

	protected SelectOperationSpec createSearchQuery(boolean strict) {
		StringBuilder builder = this.createQueryString(strict);
		return new SelectOperationSpec(builder.toString());
	}

	protected SelectOperationSpec createCountSearchQuery(boolean strict) {
		StringBuilder builder = this.createCountQueryString(strict);
		return new SelectOperationSpec(builder.toString());
	}

	protected SelectOperationSpec createSearchQuery(Map<String, ?> params, boolean strict) {
		StringBuilder builder = this.createQueryString(strict);
		this.appendWhereClausesToBuilder(builder, params);
		SelectOperationSpec query = new SelectOperationSpec(builder.toString());
		if (params != null && !params.isEmpty()) {
			query.addQueryParameters(params);
		}

		return query;
	}

	protected SelectOperationSpec createSearchQuery(SortParameters sortParameters, boolean strict) {
		StringBuilder builder = this.createQueryString(strict);
		this.appendOrderByClausesToBuilder(builder, sortParameters);
		return new SelectOperationSpec(builder.toString());
	}

	protected SelectOperationSpec createSearchQuery(Map<String, ?> params, SortParameters sortParameters, boolean strict) {
		StringBuilder builder = this.createQueryString(strict);
		this.appendWhereClausesToBuilder(builder, params);
		this.appendOrderByClausesToBuilder(builder, sortParameters);
		SelectOperationSpec query = new SelectOperationSpec(builder.toString());
		if (params != null && !params.isEmpty()) {
			query.addQueryParameters(params);
		}
		return query;
	}


	protected PageableSelectOperationSpec createSearchQuery(Map<String, ?> params, SortParameters sortParameters, long count, boolean strict) {
		StringBuilder builder = this.createQueryString(strict);
		this.appendWhereClausesToBuilder(builder, params);
		this.appendOrderByClausesToBuilder(builder, sortParameters);
		var query = new PageableSelectOperationSpec(builder.toString());
		if (count > 0) {
			query.setCount(count);
		}

		if (params != null && !params.isEmpty()) {
			query.addQueryParameters(params);
		}

		return query;
	}

	protected PageableSelectOperationSpec createSearchQuery(Map<String, ?> params, SortParameters sortParameters, long count, long page, boolean strict) {
		StringBuilder builder = this.createQueryString(strict);
		this.appendWhereClausesToBuilder(builder, params);
		this.appendOrderByClausesToBuilder(builder, sortParameters);
		var query = new PageableSelectOperationSpec(builder.toString());
		query.setCount(count);
		query.setPage(page);

		if (params != null && !params.isEmpty()) {
			query.addQueryParameters(params);
		}

		return query;
	}

	protected StringBuilder createQueryString(boolean strict, String typecode) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * ");
		builder.append("FROM \"");

		if (strict) {
			builder.append("#");
		}
		builder.append(typecode).append("\" AS c ");
		return builder;
	}

	protected StringBuilder createCountQueryString(boolean strict, String typecode) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT count(c.uuid) ");
		builder.append("FROM \"");

		if (strict) {
			builder.append("#");
		}
		builder.append(typecode).append("\" AS c ");
		return builder;
	}

	protected StringBuilder createQueryString(boolean strict) {
		return createQueryString(strict, this.typecode);
	}

	protected StringBuilder createCountQueryString(boolean strict) {
		return createCountQueryString(strict, this.typecode);
	}

	private void appendWhereClausesToBuilder(StringBuilder builder, Map<String, ?> params) {
		if (params != null && !params.isEmpty()) {
			builder.append("WHERE ");
			boolean firstParam = true;

			for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext(); firstParam = false) {
				String paramName = iterator.next();
				if (!firstParam) {
					builder.append("AND ");
				}

				builder.append(" c.").append(paramName).append("=:").append(paramName).append(" ");
			}
		}

	}

	private void appendOrderByClausesToBuilder(StringBuilder builder, SortParameters sortParameters) {
		if (sortParameters != null && !sortParameters.isEmpty()) {
			builder.append("ORDER BY ");
			boolean firstParam = true;
			Map<String, SortOrder> sortParams = sortParameters.getSortParameters();

			for (Iterator<String> iterator = sortParams.keySet().iterator(); iterator.hasNext(); firstParam = false) {
				String name = iterator.next();
				if (!firstParam) {
					builder.append(", ");
				}

				builder.append("c.").append(name).append(" ").append(sortParams.get(name));
			}
		} else {
			builder.append("ORDER BY c.").append(GenericItem.CREATE_DATE);
		}

	}

	public SearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}
}
