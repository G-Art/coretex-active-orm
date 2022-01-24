package com.coretex.orm.core.services.bootstrap.dialect.mysql;

import com.coretex.orm.core.activeorm.query.operations.contexts.SelectItemAttributeOperationConfigContext;
import com.coretex.orm.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;

import java.util.Collections;
import java.util.HashMap;

public class MysqlLocalizedItemAttributeQueryBuilder implements QueryBuilder<SelectItemAttributeOperationConfigContext> {
	private static final String SELECT_LOCALIZED_ITEM_FIELD_BY_UUID_TEMPLATE = "select \"item\".\"localeiso\", \"item\".\"value\" FROM %s_loc as \"item\" WHERE \"item\".\"owner\" = UUID_TO_BIN(:ownerUuid) AND \"item\".\"attribute\" = UUID_TO_BIN(:uuid)";

	@Override
	public String createQuery(SelectItemAttributeOperationConfigContext context){
		var operationSpec = context.getOperationSpec();
		MetaAttributeTypeItem attribute = operationSpec.getAttribute();
		return String.format(SELECT_LOCALIZED_ITEM_FIELD_BY_UUID_TEMPLATE, attribute.getOwner().getTableName());
	}

	@Override
	public void prepareValueData(SelectItemAttributeOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		MetaAttributeTypeItem attribute = operationSpec.getAttribute();
		ItemContext ctx = operationSpec.getCtx();
		if (attribute.getLocalized()) {
			operationSpec.addQueryParameters( new HashMap<>() {{
				put(AbstractGenericItem.UUID, attribute.getUuid().toString());
				put("ownerUuid", ctx.getUuid().toString());
			}});
		} else {
			operationSpec.addQueryParameters(Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid().toString()));
		}
	}
}
