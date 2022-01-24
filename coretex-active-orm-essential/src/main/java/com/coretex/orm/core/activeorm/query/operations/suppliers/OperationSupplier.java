package com.coretex.orm.core.activeorm.query.operations.suppliers;

import com.coretex.orm.core.activeorm.query.operations.SqlOperation;
import com.coretex.orm.core.activeorm.query.operations.contexts.OperationConfigContext;

public interface OperationSupplier<O extends SqlOperation<?,?>, CTX extends OperationConfigContext<?>> {

	O get(CTX os);
}
