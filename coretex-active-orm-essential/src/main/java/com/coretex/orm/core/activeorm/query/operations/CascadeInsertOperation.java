package com.coretex.orm.core.activeorm.query.operations;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.services.AbstractJdbcService;
import com.coretex.orm.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CascadeInsertOperation extends InsertOperation {

	private Logger LOG = LoggerFactory.getLogger(CascadeInsertOperation.class);

	public CascadeInsertOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService, DbDialectService dbDialectService) {
		super(abstractJdbcService, itemOperationInterceptorService, dbDialectService);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.INSERT_CASCADE;
	}


	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}
}
