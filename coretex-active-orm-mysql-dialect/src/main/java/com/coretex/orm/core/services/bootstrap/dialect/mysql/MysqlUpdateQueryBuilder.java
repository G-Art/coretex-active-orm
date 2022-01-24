package com.coretex.orm.core.services.bootstrap.dialect.mysql;

import com.coretex.orm.core.activeorm.query.operations.contexts.UpdateOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.AbstractValueDataHolder;
import com.coretex.orm.core.activeorm.query.operations.dataholders.UpdateValueDataHolder;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.general.utils.ItemUtils;
import com.coretex.orm.core.services.bootstrap.dialect.AbstractUpdateQueryBuilder;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.coretex.orm.core.general.utils.ItemUtils.getTypeCode;

public class MysqlUpdateQueryBuilder extends AbstractUpdateQueryBuilder<UpdateOperationConfigContext> {
	public final static String DEFAULT_UPDATE_ITEM_QUERY = "update %s set %s where uuid = UUID_TO_BIN(:uuid)";

	@Override
	public String createQuery(UpdateOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();
		var metaTypeProvider = operationSpec.getMetaTypeProvider();
		Collection<? extends AbstractValueDataHolder<?>> entries = context.getOperationSpec().getValueDataHolders().values();
		operationSpec.addValueDataHolder(new UpdateValueDataHolder(metaTypeProvider.findAttribute(ItemUtils.getTypeCode(item), AbstractGenericItem.UUID), operationSpec));
		return String.format(DEFAULT_UPDATE_ITEM_QUERY, context.getOperationSpec().getItem().getMetaType().getTableName(),
				entries.stream()
						.map(entry -> {
							MetaAttributeTypeItem attributeTypeItem = entry.getAttributeTypeItem();
							if (!AttributeTypeUtils.isRegularTypeAttribute(attributeTypeItem)) {
								return String.format("%s = UUID_TO_BIN(:%s)", entry.columnName(), entry.getAttributeName());
							} else {
								RegularTypeItem regularTypeItem = (RegularTypeItem) attributeTypeItem.getAttributeType();
								if (regularTypeItem.getPersistenceType().equalsIgnoreCase(AbstractGenericItem.UUID)) {
									return String.format("%s = UUID_TO_BIN(:%s)", entry.columnName(), entry.getAttributeName());
								}
							}
							return String.format("%s = :%s", entry.columnName(), entry.getAttributeName());
						})
						.collect(Collectors.joining(",")));

	}
}
