package com.coretex.orm.core.services.bootstrap.dialect.mysql;

import com.coretex.orm.core.activeorm.query.operations.contexts.RemoveOperationConfigContext;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.services.bootstrap.dialect.AbstractRemoveQueryBuilder;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;

import java.util.stream.Collectors;

public class MysqlRemoveQueryBuilder extends AbstractRemoveQueryBuilder<RemoveOperationConfigContext> {

	@Override
	public String createQuery(RemoveOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();

		return String.format(AbstractRemoveQueryBuilder.DEFAULT_DELETE_ITEM_QUERY, item.getMetaType().getTableName(),
				operationSpec.getValueDataHolders()
						.values()
						.stream()
						.map(entry -> {
							MetaAttributeTypeItem attributeTypeItem = entry.getAttributeTypeItem();
							if(!AttributeTypeUtils.isRegularTypeAttribute(attributeTypeItem)){
								return String.format("%s = UUID_TO_BIN(:%s)", entry.columnName(), entry.getAttributeName());
							}else {
								RegularTypeItem regularTypeItem = (RegularTypeItem) attributeTypeItem.getAttributeType();
								if (regularTypeItem.getPersistenceType().equalsIgnoreCase("uuid")) {
									return String.format("%s = UUID_TO_BIN(:%s)", entry.columnName(), entry.getAttributeName());
								}
							}
							return String.format("%s = :%s", entry.columnName(), entry.getAttributeName());
						})
						.collect(Collectors.joining(" AND ")));

	}
}
