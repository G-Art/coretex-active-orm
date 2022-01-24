package com.coretex.orm.core.activeorm.query.operations.contexts;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.specs.RemoveOperationSpec;
import com.coretex.orm.core.activeorm.services.ReactiveSearchResult;

import java.util.stream.Stream;

public class RemoveOperationConfigContext
		extends AbstractOperationConfigContext<RemoveOperationSpec> {

	public RemoveOperationConfigContext(RemoveOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.DELETE;
	}

	@Override
	public <R extends ReactiveSearchResult<T>, T> R wrapResult(Stream<T> result) {
		return (R) new ReactiveSearchResult<>(() -> result);
	}
}
