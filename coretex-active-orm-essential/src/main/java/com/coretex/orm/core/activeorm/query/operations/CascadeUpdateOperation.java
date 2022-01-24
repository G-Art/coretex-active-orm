package com.coretex.orm.core.activeorm.query.operations;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.services.AbstractJdbcService;
import com.coretex.orm.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CascadeUpdateOperation extends UpdateOperation {

	private Logger LOG = LoggerFactory.getLogger(CascadeUpdateOperation.class);

	public CascadeUpdateOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService, DbDialectService dbDialectService) {
		super(abstractJdbcService, itemOperationInterceptorService, dbDialectService);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.DELETE_CASCADE;
	}

	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}
}
