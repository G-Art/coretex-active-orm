package com.coretex.orm.core.activeorm.query.operations;

import com.coretex.orm.core.activeorm.cache.CacheService;
import com.coretex.orm.core.activeorm.cache.impl.FeaturedStatementCacheContext;
import com.coretex.orm.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.operations.contexts.SelectOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.orm.core.activeorm.query.operations.sources.SelectSqlParameterSource;
import com.coretex.orm.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.LogManager;
import java.util.stream.Stream;

public class SelectOperation
		extends SqlOperation<SelectOperationSpec, SelectOperationConfigContext> {

	@Value("${db.query.cache.enable:true}")
	private boolean queryCacheEnabled;

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectOperation.class);

	private static final Cache<Integer, QueryInfoHolder> selectCache = CacheBuilder.newBuilder()
			.softValues()
			.maximumSize(512)
			.concurrencyLevel(1)
			.expireAfterAccess(20, TimeUnit.SECONDS)
			.build();

	private final QueryTransformationProcessor<QueryInfoHolder> transformationProcessor;
	private final CoretexContext coretexContext;
	private final ResultSetExtractor<Stream<?>> extractor;

	private final CacheService cacheService;

	protected final Function<SelectOperationSpec, FeaturedStatementCacheContext> searchQuerySupplier;

	public SelectOperation(QueryTransformationProcessor<QueryInfoHolder> transformationProcessor,
	                       CoretexContext coretexContext, ResultSetExtractor<Stream<?>> extractor, CacheService cacheService) {
		this.transformationProcessor = transformationProcessor;
		this.coretexContext = coretexContext;
		this.extractor = extractor;
		this.cacheService = cacheService;
		searchQuerySupplier = (operationSpec) -> {
			var query = operationSpec.getQuery();
			if (queryCacheEnabled) {
				try {
					var selectQueryInfoHolder = selectCache.get(Objects.hashCode(query), () -> transformationProcessor.execute(query));
					if(Objects.nonNull(selectQueryInfoHolder.getValidationThrowable())){
						throw new RuntimeException(selectQueryInfoHolder.getValidationThrowable());
					}
					operationSpec.setTransformedQuery(selectQueryInfoHolder.getResultQuery());
					return new FeaturedStatementCacheContext(selectQueryInfoHolder, operationSpec.getParameters());
				} catch (ExecutionException e) {
					LOGGER.error("Cache calculation error", e);
				}
			}else {
				LOGGER.warn("Query cache disabled, it might affect performance. Consider to set \"true\" for parameter [db.query.cache.enable]");
			}
			LogManager.getLogManager();
			var queryInfoHolder = this.transformationProcessor.execute(query);
			if(Objects.nonNull(queryInfoHolder.getValidationThrowable())){
				throw new RuntimeException(queryInfoHolder.getValidationThrowable());
			}
			operationSpec.setTransformedQuery(queryInfoHolder.getResultQuery());
			return new FeaturedStatementCacheContext(queryInfoHolder, operationSpec.getParameters());

		};
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.SELECT;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Stream<T> execute(SelectOperationConfigContext operationConfigContext) {
		return (Stream<T>) searchQuerySupplier
				.andThen(queryStatementContext -> cacheService.get(queryStatementContext, () -> {
					var query = operationConfigContext.getOperationSpec().getTransformedQuery();

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
					}

					var customExtractor = operationConfigContext.customExtractor();
					return getJdbcTemplate().query(query,
							new SelectSqlParameterSource(operationConfigContext.getOperationSpec(), coretexContext),
							customExtractor.orElse(extractor));
				}))
				.apply(operationConfigContext.getOperationSpec());

	}

	protected ResultSetExtractor<Stream<?>> getExtractor() {
		return extractor;
	}
}
