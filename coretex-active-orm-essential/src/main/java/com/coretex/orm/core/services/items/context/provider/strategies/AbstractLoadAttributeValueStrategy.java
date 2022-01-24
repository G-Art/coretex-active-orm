package com.coretex.orm.core.services.items.context.provider.strategies;

import com.coretex.orm.core.activeorm.query.ActiveOrmOperationExecutor;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;


public abstract class AbstractLoadAttributeValueStrategy implements LoadAttributeValueStrategy {

	private CoretexContext coretexContext;
	private DbDialectService dbDialectService;

	public AbstractLoadAttributeValueStrategy(CoretexContext coretexContext, DbDialectService dbDialectService) {
		this.coretexContext = coretexContext;
		this.dbDialectService = dbDialectService;
	}

	public abstract ActiveOrmOperationExecutor getOperationExecutor();

	public CoretexContext getCortexContext() {
		return coretexContext;
	}

	public DbDialectService getDbDialectService() {
		return dbDialectService;
	}
}
