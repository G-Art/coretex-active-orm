package com.coretex.orm.core.activeorm.query.operations.contexts;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.specs.LocalizedDataSaveOperationSpec;
import com.coretex.orm.core.activeorm.services.ReactiveSearchResult;

import java.util.stream.Stream;

import static com.coretex.orm.core.activeorm.query.QueryType.LOCALIZED_DATA_SAVE;

public class LocalizedDataSaveOperationConfigContext
		extends AbstractOperationConfigContext<LocalizedDataSaveOperationSpec> {

	public LocalizedDataSaveOperationConfigContext(LocalizedDataSaveOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return LOCALIZED_DATA_SAVE;
	}

	@Override
	public <R extends ReactiveSearchResult<T>, T> R wrapResult(Stream<T> result) {
		return (R) new ReactiveSearchResult<>(() -> result);
	}
}
