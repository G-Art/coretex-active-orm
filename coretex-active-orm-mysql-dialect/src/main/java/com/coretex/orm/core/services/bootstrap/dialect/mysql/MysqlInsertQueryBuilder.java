package com.coretex.orm.core.services.bootstrap.dialect.mysql;

import com.coretex.orm.core.activeorm.query.operations.contexts.InsertOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.AbstractValueDataHolder;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.services.bootstrap.dialect.AbstractInsertQueryBuilder;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;

import java.util.stream.Collectors;

public class MysqlInsertQueryBuilder extends AbstractInsertQueryBuilder<InsertOperationConfigContext> {

	@Override
	public String createQuery(InsertOperationConfigContext context){
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();

		var entries = operationSpec.getValueDataHolders().values();
		return String.format(AbstractInsertQueryBuilder.DEFAULT_INSERT_ITEM_QUERY, item.getMetaType().getTableName(),
				entries.stream().map(AbstractValueDataHolder::columnName).collect(Collectors.joining(",")),
				entries.stream()
						.map(entry -> {
							MetaAttributeTypeItem attributeTypeItem = entry.getAttributeTypeItem();
							if(!AttributeTypeUtils.isRegularTypeAttribute(attributeTypeItem)){
								return String.format("UUID_TO_BIN(:%s)", entry.getAttributeName());
							}else {
								RegularTypeItem regularTypeItem = (RegularTypeItem) attributeTypeItem.getAttributeType();
								if (regularTypeItem.getPersistenceType().equalsIgnoreCase(AbstractGenericItem.UUID)) {
									return String.format("UUID_TO_BIN(:%s)", entry.getAttributeName());
								}
							}
							return String.format(":%s", entry.getAttributeName());
						})
						.collect(Collectors.joining(",")));
	}
}
