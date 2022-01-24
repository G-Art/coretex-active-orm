package com.coretex.orm.core.activeorm.query.operations;

import com.coretex.orm.core.activeorm.cache.CacheService;
import com.coretex.orm.core.activeorm.query.ActiveOrmOperationExecutor;
import com.coretex.orm.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.orm.core.activeorm.query.operations.contexts.SelectOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.orm.core.activeorm.query.specs.select.PageableSelectOperationSpec;
import com.coretex.orm.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PageableSelectOperation extends SelectOperation {

	private static final Logger LOGGER = LoggerFactory.getLogger(PageableSelectOperation.class);

	private final CoretexContext coretexContext;


	public PageableSelectOperation(QueryTransformationProcessor<QueryInfoHolder> transformationProcessor,
	                               CoretexContext coretexContext, ResultSetExtractor<Stream<?>> extractor, CacheService cacheService) {
		super(transformationProcessor, coretexContext, extractor, cacheService);
		this.coretexContext = coretexContext;
	}

	@Override
	public <T> Stream<T> execute(SelectOperationConfigContext operationConfigContext) {
		var execute = super.<T>execute(operationConfigContext);

		searchQuerySupplier.andThen(query -> {
			var selectOperationSpec = (PageableSelectOperationSpec)operationConfigContext.getOperationSpec();
			String countQuery = getTotalCountQuery(selectOperationSpec.getOriginalQuery());
			SelectOperationSpec operationSpec = new SelectOperationSpec(countQuery, operationConfigContext.getOperationSpec().getParameters());
			var results = getOrmOperationExecutor().execute(operationSpec.createOperationContext()).getResultStream().collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(results)) {
				Map rowResultMap = (Map) results.iterator().next();
				operationConfigContext.setTotalCount((Long) rowResultMap.get("count"));
			}
			return null;
		}).apply(operationConfigContext.getOperationSpec());

		return execute;
	}

	private String getTotalCountQuery(String query) {
		return String.format("SELECT count(*) as \"count\" FROM (%s) as co", query);
	}

	@Lookup
	public ActiveOrmOperationExecutor getOrmOperationExecutor(){
		return null;
	}

}
