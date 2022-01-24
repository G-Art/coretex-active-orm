package com.coretex.orm.core.services.bootstrap.dialect.mysql;

import com.coretex.orm.core.CoretexConfigurationProvider;
import com.coretex.orm.core.activeorm.query.operations.contexts.SelectItemAttributeOperationConfigContext;
import com.coretex.orm.core.activeorm.query.specs.select.SelectItemAttributeOperationSpec;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.orm.core.services.bootstrap.impl.MetaTypeProvider;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MysqlRegularAttributeQueryBuilder implements QueryBuilder<SelectItemAttributeOperationConfigContext> {

	private final Logger LOG = LoggerFactory.getLogger(MysqlRegularAttributeQueryBuilder.class);

	private final MysqlLocalizedItemAttributeQueryBuilder localizedItemAttributeQueryBuilder = new MysqlLocalizedItemAttributeQueryBuilder();

	private static final String SELECT_REGULAR_FIELD_BY_UUID_TEMPLATE = "select %s from \"%s\" as \"item\" where \"item\".\"uuid\" = UUID_TO_BIN(:uuid)";

	@Override
	public String createQuery(SelectItemAttributeOperationConfigContext context) {
		SelectItemAttributeOperationSpec operationSpec = context.getOperationSpec();
		MetaAttributeTypeItem attribute = operationSpec.getAttribute();
		ItemContext ctx = operationSpec.getCtx();
		if (attribute.getLocalized()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Prepare query for localized attribute Mysql::[{}]", attribute.getAttributeName());
			}
			return localizedItemAttributeQueryBuilder.createQuery(context);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Prepare query for attribute Mysql::[{}]", attribute.getAttributeName());
			}
			var loadedAttributes = ctx.loadedAttributes();
			MetaTypeProvider coretexContext = CoretexConfigurationProvider.getMetaTypeProvider();
			var unloadedAttributes = coretexContext.getAllAttributes(ctx.getTypeCode())
					.values()
					.stream()
					.filter(AttributeTypeUtils::isRegularTypeAttribute)
					.filter(attr -> !attr.getLocalized())
					.filter(attr -> !loadedAttributes.contains(attr.getAttributeName()))
					.collect(Collectors.toList());
			return createQuery(unloadedAttributes, ctx);
		}
	}

	@Override
	public void prepareValueData(SelectItemAttributeOperationConfigContext context) {
		SelectItemAttributeOperationSpec operationSpec = context.getOperationSpec();
		MetaAttributeTypeItem attribute = operationSpec.getAttribute();
		ItemContext ctx = operationSpec.getCtx();
		if (attribute.getLocalized()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Prepare query value data for localized attribute Mysql::[{}]", attribute.getAttributeName());
			}
			localizedItemAttributeQueryBuilder.prepareValueData(context);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Prepare query value data for attribute Mysql::[{}]", attribute.getAttributeName());
			}
			operationSpec.addQueryParameters(Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid().toString()));
		}
	}

	protected String createQuery(List<MetaAttributeTypeItem> unloadedAttributes, ItemContext ctx) {
		var columns = unloadedAttributes.stream()
				.map(attr -> {
					RegularTypeItem regularTypeItem = (RegularTypeItem) attr.getAttributeType();
					if (regularTypeItem.getPersistenceType().equalsIgnoreCase(AbstractGenericItem.UUID)) {
						return String.format("BIN_TO_UUID(\"item\".\"%s\") as \"%s\"", attr.getAttributeName(), attr.getColumnName());
					}
					return String.format("\"item\".\"%s\"", attr.getAttributeName());
				})
				.collect(Collectors.joining(","));
		return String.format(SELECT_REGULAR_FIELD_BY_UUID_TEMPLATE, columns, ctx.getTypeCode());
	}
}
