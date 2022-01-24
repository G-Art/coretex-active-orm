package com.coretex.orm.core.services.bootstrap.dialect.mysql;

import com.coretex.orm.core.activeorm.query.operations.contexts.SelectItemAttributeOperationConfigContext;
import com.coretex.orm.core.activeorm.query.specs.select.SelectItemAttributeOperationSpec;
import com.coretex.orm.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class MysqlRelatedItemAttributeQueryBuilder implements QueryBuilder<SelectItemAttributeOperationConfigContext> {

	private final Logger LOG = LoggerFactory.getLogger(MysqlRelatedItemAttributeQueryBuilder.class);

	private static final String SELECT_ITEM_FIELD_BY_UUID_TEMPLATE = "select item.* from \"%s\" as item left join \"%s\" as j on (j.%s = item.uuid) where j.uuid = :uuid";


	@Override
	public String createQuery(SelectItemAttributeOperationConfigContext context) {
		SelectItemAttributeOperationSpec operationSpec = context.getOperationSpec();
		MetaAttributeTypeItem attribute = operationSpec.getAttribute();
		ItemContext ctx = operationSpec.getCtx();
		return createQuery(attribute, ctx);
	}

	@Override
	public void prepareValueData(SelectItemAttributeOperationConfigContext context) {
		SelectItemAttributeOperationSpec operationSpec = context.getOperationSpec();
		ItemContext ctx = operationSpec.getCtx();
		operationSpec.addQueryParameters(createParameters(ctx));
	}

	protected Map<String, Object> createParameters(ItemContext ctx) {
		return Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid().toString());
	}

	protected String createQuery(MetaAttributeTypeItem attribute, ItemContext ctx) {
		return String.format(SELECT_ITEM_FIELD_BY_UUID_TEMPLATE,
				((MetaTypeItem) attribute.getAttributeType()).getTypeCode(), ctx.getTypeCode(), attribute.getAttributeName());
	}

}
