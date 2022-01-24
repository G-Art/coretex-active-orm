package com.coretex.orm.core.services.bootstrap.dialect.postgres;

import com.coretex.orm.core.activeorm.query.operations.contexts.InsertOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.AbstractValueDataHolder;
import com.coretex.orm.core.services.bootstrap.dialect.AbstractInsertQueryBuilder;

import java.util.stream.Collectors;

public class PostgresInsertQueryBuilder extends AbstractInsertQueryBuilder<InsertOperationConfigContext> {

	@Override
	public String createQuery(InsertOperationConfigContext context){
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();

		var entries = operationSpec.getValueDataHolders().values();
		return String.format(AbstractInsertQueryBuilder.DEFAULT_INSERT_ITEM_QUERY, item.getMetaType().getTableName(),
				entries.stream().map(AbstractValueDataHolder::columnName).collect(Collectors.joining(",")),
				entries.stream()
						.map(entry -> String.format(":%s", entry.getAttributeName()))
						.collect(Collectors.joining(",")));
	}
}
