package com.coretex.orm.core.activeorm.query.operations.contexts;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.specs.SqlOperationSpec;
import com.coretex.orm.core.activeorm.services.ReactiveSearchResult;

import java.util.stream.Stream;

public interface OperationConfigContext<
		OS extends SqlOperationSpec> {

	OS getOperationSpec();

	QueryType getQueryType();

	<R extends ReactiveSearchResult<T>, T> R wrapResult(Stream<T> result);
}
