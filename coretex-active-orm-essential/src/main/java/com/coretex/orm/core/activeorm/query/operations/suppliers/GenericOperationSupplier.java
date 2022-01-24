package com.coretex.orm.core.activeorm.query.operations.suppliers;

import com.coretex.orm.core.activeorm.query.operations.ModificationOperation;
import com.coretex.orm.core.activeorm.query.operations.contexts.AbstractOperationConfigContext;

public class GenericOperationSupplier implements OperationSupplier<ModificationOperation<?, ?>, AbstractOperationConfigContext<?>> {

	private final ModificationOperation<?, ?> operation;

	public GenericOperationSupplier(ModificationOperation<?, ?> operation) {
		this.operation = operation;
	}

	@Override
	public ModificationOperation<?, ?> get(AbstractOperationConfigContext<?> abstractOperationConfigContext) {
		return operation;
	}

}
