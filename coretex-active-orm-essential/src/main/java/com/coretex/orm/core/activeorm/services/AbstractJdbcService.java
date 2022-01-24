package com.coretex.orm.core.activeorm.services;

import com.coretex.orm.core.activeorm.query.ActiveOrmOperationExecutor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

public abstract class AbstractJdbcService {


	public TransactionTemplate getTransactionTemplate() {
		return null;
	}

	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return null;
	}

	public ActiveOrmOperationExecutor getOrmOperationExecutor(){
		return null;
	}

	public void executeInTransaction(Supplier<TransactionCallbackWithoutResult> callbackWithoutResultSupplier) {
		getTransactionTemplate().execute(callbackWithoutResultSupplier.get());
	}
}
