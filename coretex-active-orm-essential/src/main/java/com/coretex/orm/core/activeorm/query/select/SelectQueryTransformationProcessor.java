package com.coretex.orm.core.activeorm.query.select;

import com.coretex.orm.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.orm.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.orm.core.activeorm.query.select.optimizer.SelectQueryOptimizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SelectQueryTransformationProcessor implements QueryTransformationProcessor<QueryInfoHolder> {

	private Logger LOG = LoggerFactory.getLogger(SelectQueryTransformationProcessor.class);
	private SelectQueryOptimizer selectQueryOptimizer;


	public SelectQueryTransformationProcessor(SelectQueryOptimizer selectQueryOptimizer) {
		this.selectQueryOptimizer = selectQueryOptimizer;
	}

	@Override
	public QueryInfoHolder execute(String query) {
		QueryInfoHolder queryInfoHolder = new QueryInfoHolder(query);
		LOG.debug("Optimize query [" + query + "]");
		selectQueryOptimizer.process(queryInfoHolder);
		LOG.debug("Query optimized [" + query + "], result :: [" + queryInfoHolder.getResultQuery() + "]");
		return queryInfoHolder;
	}
}
