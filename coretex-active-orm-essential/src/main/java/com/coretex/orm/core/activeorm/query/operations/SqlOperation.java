package com.coretex.orm.core.activeorm.query.operations;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.orm.core.activeorm.query.specs.SqlOperationSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class SqlOperation<
		OS extends SqlOperationSpec,
		CTX extends OperationConfigContext<OS>> {

	private Logger LOG = LoggerFactory.getLogger(SqlOperation.class);

	public abstract QueryType getQueryType();

	public abstract <T> Stream<T> execute(CTX operationConfigContext);

	@Lookup
	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return null;
	}

	protected void executeJdbcOperation(Consumer<NamedParameterJdbcTemplate> jdbcTemplateConsumer) {
		jdbcTemplateConsumer.accept(getJdbcTemplate());
	}


}
