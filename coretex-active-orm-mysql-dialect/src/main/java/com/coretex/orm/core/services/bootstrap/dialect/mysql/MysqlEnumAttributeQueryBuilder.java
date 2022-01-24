package com.coretex.orm.core.services.bootstrap.dialect.mysql;

import com.coretex.orm.core.activeorm.exceptions.SearchException;
import com.coretex.orm.core.activeorm.query.operations.contexts.SelectItemAttributeOperationConfigContext;
import com.coretex.orm.core.activeorm.query.specs.select.SelectItemAttributeOperationSpec;
import com.coretex.orm.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class MysqlEnumAttributeQueryBuilder implements QueryBuilder<SelectItemAttributeOperationConfigContext> {

	private final Logger LOG = LoggerFactory.getLogger(MysqlEnumAttributeQueryBuilder.class);

	private static final String SELECT_ENUM_FIELD_BY_UUID_TEMPLATE = "select %s from \"%s\" as \"item\" where \"item\".\"uuid\" = UUID_TO_BIN(:uuid)";


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
		return String.format(SELECT_ENUM_FIELD_BY_UUID_TEMPLATE,
				"BIN_TO_UUID(\"item\"." + attribute.getColumnName() + ") as \"" + attribute.getColumnName() +"\"",
				ctx.getTypeCode());
	}

	protected static String getColumnName(MetaRelationTypeItem metaRelationTypeItem, String attributeName) {
		return metaRelationTypeItem.getItemAttributes().stream()
				.filter(metaAttr -> metaAttr.getAttributeName().equals(attributeName))
				.findFirst()
				.orElseThrow(() -> new SearchException(String.format("Column for attribute [%s] is not exist", attributeName))).getColumnName();
	}

}
