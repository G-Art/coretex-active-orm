package com.coretex.orm.core.services.bootstrap.dialect.postgres;

import com.coretex.orm.core.activeorm.query.operations.contexts.RemoveOperationConfigContext;
import com.coretex.orm.core.services.bootstrap.dialect.AbstractRemoveQueryBuilder;

import java.util.stream.Collectors;

public class PostgresRemoveQueryBuilder extends AbstractRemoveQueryBuilder<RemoveOperationConfigContext> {

	@Override
	public String createQuery(RemoveOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();

		return String.format(AbstractRemoveQueryBuilder.DEFAULT_DELETE_ITEM_QUERY, item.getMetaType().getTableName(),
				operationSpec.getValueDataHolders()
						.values()
						.stream()
						.map(key -> String.format("%s = :%s", key.columnName(), key.getAttributeName()))
						.collect(Collectors.joining(" AND ")));

	}
}
