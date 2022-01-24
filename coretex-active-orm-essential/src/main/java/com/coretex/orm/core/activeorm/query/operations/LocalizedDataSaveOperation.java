package com.coretex.orm.core.activeorm.query.operations;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.operations.contexts.LocalizedDataSaveOperationConfigContext;
import com.coretex.orm.core.activeorm.query.specs.LocalizedDataSaveOperationSpec;
import com.coretex.orm.core.activeorm.services.AbstractJdbcService;
import com.coretex.orm.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.orm.core.services.bootstrap.dialect.AbstractLocalizedDataSaveQueryBuilder;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

public class LocalizedDataSaveOperation extends ModificationOperation<LocalizedDataSaveOperationSpec, LocalizedDataSaveOperationConfigContext> {

	private Logger LOG = LoggerFactory.getLogger(LocalizedDataSaveOperation.class);
	private DbDialectService dbDialectService;

	public LocalizedDataSaveOperation(AbstractJdbcService abstractJdbcService, DbDialectService dbDialectService, ItemOperationInterceptorService itemOperationInterceptorService) {
		super(abstractJdbcService, itemOperationInterceptorService, dbDialectService);
		this.dbDialectService = dbDialectService;
	}

	@Override
	protected void executeBefore(LocalizedDataSaveOperationConfigContext operationConfigContext) {
		//not required
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.LOCALIZED_DATA_SAVE;
	}

	@Override
	public void executeOperation(LocalizedDataSaveOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		buildQuery(operationConfigContext);
		AbstractLocalizedDataSaveQueryBuilder.LocalizedAttributeSaveFetcher fetcher = operationSpec.getFetcher();

		if(fetcher.hasValuesForInsert()){
			Map<Locale, Object> insertValues = fetcher.getInsertValues();
			var query = operationSpec.getInsertQuery();
			if(LOG.isDebugEnabled()){
				LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
			}
			executeJdbcOperation(jdbcTemplate -> jdbcTemplate.batchUpdate(query, fetcher.buildParams(insertValues, operationSpec)));
		}
		if(fetcher.hasValuesForUpdate()){
			Map<Locale, Object> updateValues = fetcher.getUpdateValues();
			var query = operationSpec.getUpdateQuery();
			if(LOG.isDebugEnabled()){
				LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
			}
			executeJdbcOperation(jdbcTemplate -> jdbcTemplate.batchUpdate(query, fetcher.buildParams(updateValues, operationSpec)));
		}
	}

	@Override
	protected void executeAfter(LocalizedDataSaveOperationConfigContext operationConfigContext) {
		//not required
	}

	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}

	@Override
	protected boolean useInterceptors(LocalizedDataSaveOperationConfigContext operationConfigContext) {
		return false;
	}
}
