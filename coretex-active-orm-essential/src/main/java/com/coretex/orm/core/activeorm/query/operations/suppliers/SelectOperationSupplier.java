package com.coretex.orm.core.activeorm.query.operations.suppliers;

import com.coretex.orm.core.activeorm.query.operations.SelectOperation;
import com.coretex.orm.core.activeorm.query.operations.contexts.SelectOperationConfigContext;

public class SelectOperationSupplier implements OperationSupplier<SelectOperation, SelectOperationConfigContext> {

	private final SelectOperation selectOperation;
	private final SelectOperation pageableSelectOperation;

	public SelectOperationSupplier(SelectOperation selectOperation, SelectOperation pageableSelectOperation) {
		this.selectOperation = selectOperation;
		this.pageableSelectOperation = pageableSelectOperation;
	}

	@Override
	public SelectOperation get(SelectOperationConfigContext selectOperationSpec) {
		if(selectOperationSpec.isPageable()){
			return pageableSelectOperation;
		}
		return selectOperation;
	}

}
