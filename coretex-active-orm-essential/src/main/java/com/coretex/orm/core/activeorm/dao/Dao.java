package com.coretex.orm.core.activeorm.dao;

import com.coretex.orm.core.activeorm.services.PageableSearchResult;
import com.coretex.orm.meta.AbstractGenericItem;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface Dao<I extends AbstractGenericItem> {

	void save(I item);

	void delete(I item);

	Long count();
	Long count(boolean strict);

	List<I> find();
	List<I> find(boolean strict);

	Stream<I> findReactive();
	Stream<I> findReactive(boolean strict);

	I find(UUID uuid);
	I find(UUID uuid, boolean strict);

	I findSingle(Map<String, ?> params, boolean throwAmbiguousException);
	I findSingle(Map<String, ?> params, boolean throwAmbiguousException, boolean strict);

	Optional<I> findOne(Map<String, ?> params, boolean throwAmbiguousException);
	Optional<I> findOne(Map<String, ?> params, boolean throwAmbiguousException, boolean strict);

	List<I> find(String query);
	Stream<I> findReactive(String query);

	List<I> find(String query, Map<String, Object> params);
	Stream<I> findReactive(String query, Map<String, Object> params);

	List<I> find(Map<String, ?> params);
	List<I> find(Map<String, ?> params, boolean strict);

	Stream<I> findReactive(Map<String, ?> params);
	Stream<I> findReactive(Map<String, ?> params, boolean strict);

	List<I> find(SortParameters sortParameters);
	List<I> find(SortParameters sortParameters, boolean strict);

	Stream<I> findReactive(SortParameters sortParameters);
	Stream<I> findReactive(SortParameters sortParameters, boolean strict);

	List<I> find(Map<String, ?> params, SortParameters sortParameters);
	List<I> find(Map<String, ?> params, SortParameters sortParameters, boolean strict);

	Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters);
	Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters, boolean strict);

	List<I> find(Map<String, ?> params, SortParameters sortParameters, long count);
	List<I> find(Map<String, ?> params, SortParameters sortParameters, long count, boolean strict);

	Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count);
	Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count, boolean strict);

	List<I> find(Map<String, ?> params, SortParameters sortParameters, long count, long page);
	List<I> find(Map<String, ?> params, SortParameters sortParameters, long count, long page, boolean strict);

	Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count, long page);
	Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count, long page, boolean strict);

	PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count);
	PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count, boolean strict);

	PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count, long page);
	PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count, long page, boolean strict);

	PageableSearchResult<I> findPageable(Map<String, ?> params, long count);
	PageableSearchResult<I> findPageable(Map<String, ?> params, long count, boolean strict);

	PageableSearchResult<I> findPageable(Map<String, ?> params, long count, long page);
	PageableSearchResult<I> findPageable(Map<String, ?> params, long count, long page, boolean strict);

	PageableSearchResult<I> findPageable(Map<String, ?> params);
	PageableSearchResult<I> findPageable(Map<String, ?> params, boolean strict);

	PageableSearchResult<I> findPageable(long count, long page);
	PageableSearchResult<I> findPageable(long count, long page, boolean strict);

	PageableSearchResult<I> findPageable(long count);
	PageableSearchResult<I> findPageable(long count, boolean strict);

	PageableSearchResult<I> findPageable();
	PageableSearchResult<I> findPageable(boolean strict);
}
