package com.coretex.orm.core.activeorm.query.operations.contexts;

import com.coretex.orm.core.activeorm.query.specs.SqlOperationSpec;

public abstract class AbstractOperationConfigContext<
		OS extends SqlOperationSpec>
		implements OperationConfigContext<OS> {

	private final OS operationSpec;

	public AbstractOperationConfigContext(OS operationSpec) {
		this.operationSpec = operationSpec;
	}

	@Override
	public OS getOperationSpec() {
		return operationSpec;
	}


}
