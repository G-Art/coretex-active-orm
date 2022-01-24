package com.coretex.orm.core.services.bootstrap.dialect.postgres;

import com.coretex.orm.core.activeorm.query.operations.contexts.UpdateOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.AbstractValueDataHolder;
import com.coretex.orm.core.activeorm.query.operations.dataholders.UpdateValueDataHolder;
import com.coretex.orm.core.general.utils.ItemUtils;
import com.coretex.orm.core.services.bootstrap.dialect.AbstractUpdateQueryBuilder;
import com.coretex.orm.meta.AbstractGenericItem;

import java.util.Collection;
import java.util.stream.Collectors;


public class PostgresUpdateQueryBuilder extends AbstractUpdateQueryBuilder<UpdateOperationConfigContext> {

	@Override
	public String createQuery(UpdateOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();
		var metaTypeProvider = operationSpec.getMetaTypeProvider();
		Collection<? extends AbstractValueDataHolder<?>> entries = context.getOperationSpec().getValueDataHolders().values();
		operationSpec.addValueDataHolder(new UpdateValueDataHolder(metaTypeProvider.findAttribute(ItemUtils.getTypeCode(item), AbstractGenericItem.UUID), operationSpec));
		return String.format(AbstractUpdateQueryBuilder.DEFAULT_UPDATE_ITEM_QUERY, context.getOperationSpec().getItem().getMetaType().getTableName(),
				entries.stream()
						.map(e -> String.format("%s = :%s", e.columnName(), e.getAttributeName()))
						.collect(Collectors.joining(",")));

	}
}
