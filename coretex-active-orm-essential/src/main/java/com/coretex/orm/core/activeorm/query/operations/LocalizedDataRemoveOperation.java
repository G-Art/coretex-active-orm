package com.coretex.orm.core.activeorm.query.operations;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.operations.contexts.LocalizedDataRemoveOperationConfigContext;
import com.coretex.orm.core.activeorm.query.specs.LocalizedDataRemoveOperationSpec;
import com.coretex.orm.core.activeorm.services.AbstractJdbcService;
import com.coretex.orm.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class LocalizedDataRemoveOperation extends ModificationOperation<LocalizedDataRemoveOperationSpec, LocalizedDataRemoveOperationConfigContext> {

	private Logger LOG = LoggerFactory.getLogger(LocalizedDataRemoveOperation.class);

	public LocalizedDataRemoveOperation(AbstractJdbcService abstractJdbcService, DbDialectService dbDialectService, ItemOperationInterceptorService itemOperationInterceptorService) {
		super(abstractJdbcService, itemOperationInterceptorService, dbDialectService);
	}

	@Override
	protected void executeBefore(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		//not required
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.LOCALIZED_DATA_DELETE;
	}

	@Override
	public void executeOperation(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		var query = buildQuery(operationConfigContext);
		if(LOG.isDebugEnabled()){
			LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
		}
		executeJdbcOperation(jdbcTemplate -> jdbcTemplate.update(query, new MapSqlParameterSource(operationSpec.getParams())));
	}

	@Override
	protected void executeAfter(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		//not required
	}

	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}

	@Override
	protected boolean useInterceptors(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		return false;
	}
}
