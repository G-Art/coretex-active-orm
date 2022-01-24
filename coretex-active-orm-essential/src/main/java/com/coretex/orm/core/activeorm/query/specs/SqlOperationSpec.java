package com.coretex.orm.core.activeorm.query.specs;

import com.coretex.orm.core.activeorm.query.operations.contexts.OperationConfigContext;


public abstract class SqlOperationSpec {

	private boolean nativeQuery = false;

	public SqlOperationSpec() {
	}

	public boolean isNativeQuery() {
		return nativeQuery;
	}

	protected void setNativeQuery(boolean nativeQuery) {
		this.nativeQuery = nativeQuery;
	}

	public abstract <CTX extends OperationConfigContext<?>> CTX createOperationContext();

}
