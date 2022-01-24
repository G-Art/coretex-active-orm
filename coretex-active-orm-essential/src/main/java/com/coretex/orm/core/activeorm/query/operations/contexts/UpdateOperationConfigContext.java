package com.coretex.orm.core.activeorm.query.operations.contexts;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.specs.UpdateOperationSpec;
import com.coretex.orm.core.activeorm.services.ReactiveSearchResult;

import java.util.stream.Stream;

public class UpdateOperationConfigContext
		extends AbstractOperationConfigContext<UpdateOperationSpec> {

	public UpdateOperationConfigContext(UpdateOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.UPDATE;
	}

	@Override
	public <R extends ReactiveSearchResult<T>, T> R wrapResult(Stream<T> result) {
		return (R) new ReactiveSearchResult<>(() -> result);
	}
}
