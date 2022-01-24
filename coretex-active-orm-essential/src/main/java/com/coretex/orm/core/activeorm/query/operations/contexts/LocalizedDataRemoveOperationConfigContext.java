package com.coretex.orm.core.activeorm.query.operations.contexts;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.specs.LocalizedDataRemoveOperationSpec;
import com.coretex.orm.core.activeorm.services.ReactiveSearchResult;

import java.util.stream.Stream;

public class LocalizedDataRemoveOperationConfigContext
		extends AbstractOperationConfigContext<LocalizedDataRemoveOperationSpec> {

	public LocalizedDataRemoveOperationConfigContext(LocalizedDataRemoveOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.LOCALIZED_DATA_DELETE;
	}

	@Override
	public <R extends ReactiveSearchResult<T>, T> R wrapResult(Stream<T> result) {
		return null;
	}
}
