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

public class MysqlRelationAttributeQueryBuilder implements QueryBuilder<SelectItemAttributeOperationConfigContext> {

	private final Logger LOG = LoggerFactory.getLogger(MysqlRelationAttributeQueryBuilder.class);

	private static final String SELECT_RELATION_ITEM_FIELD_BY_UUID_TEMPLATE = "select \"item\".* FROM \"%s\" as \"item\" LEFT JOIN \"%s\" as \"rel\" ON \"item\".uuid = \"rel\".%s where \"rel\".%s = UUID_TO_BIN(:uuid) ORDER BY \"rel\".createDate";


	@Override
	public String createQuery(SelectItemAttributeOperationConfigContext context) {
		SelectItemAttributeOperationSpec operationSpec = context.getOperationSpec();
		MetaAttributeTypeItem attribute = operationSpec.getAttribute();
		return createQuery(attribute);
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

	protected String createQuery(MetaAttributeTypeItem attribute) {
		MetaRelationTypeItem metaRelationTypeItem = (MetaRelationTypeItem) attribute.getAttributeType();
		String targetColumnName = getColumnName(metaRelationTypeItem, "target");
		String sourceColumnName = getColumnName(metaRelationTypeItem, "source");
		return String.format(SELECT_RELATION_ITEM_FIELD_BY_UUID_TEMPLATE,
				attribute.getSource() ? metaRelationTypeItem.getSourceType().getTypeCode() : metaRelationTypeItem.getTargetType().getTypeCode(),
				metaRelationTypeItem.getTypeCode(),
				attribute.getSource() ? targetColumnName : sourceColumnName,
				attribute.getSource() ? sourceColumnName : targetColumnName);
	}

	protected static String getColumnName(MetaRelationTypeItem metaRelationTypeItem, String attributeName) {
		return metaRelationTypeItem.getItemAttributes().stream()
				.filter(metaAttr -> metaAttr.getAttributeName().equals(attributeName))
				.findFirst()
				.orElseThrow(() -> new SearchException(String.format("Column for attribute [%s] is not exist", attributeName))).getColumnName();
	}


}
