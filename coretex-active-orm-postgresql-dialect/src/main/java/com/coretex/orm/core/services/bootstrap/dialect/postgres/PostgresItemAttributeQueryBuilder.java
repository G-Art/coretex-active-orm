package com.coretex.orm.core.services.bootstrap.dialect.postgres;

import com.coretex.orm.core.activeorm.query.operations.contexts.SelectItemAttributeOperationConfigContext;
import com.coretex.orm.core.services.bootstrap.dialect.AbstractItemAttributeQueryBuilder;
import com.coretex.orm.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.orm.core.services.items.exceptions.NotFoundAttributeLoaderException;
import com.coretex.items.core.*;
import com.google.common.collect.Maps;

import java.util.Map;

public class PostgresItemAttributeQueryBuilder extends AbstractItemAttributeQueryBuilder {

	private final Map<String, QueryBuilder<SelectItemAttributeOperationConfigContext>> attributeTypeQueryBuildersMap;

	public PostgresItemAttributeQueryBuilder() {
		attributeTypeQueryBuildersMap = Maps.newHashMap();
		attributeTypeQueryBuildersMap.put(RegularTypeItem.ITEM_TYPE, new PostgresRegularAttributeQueryBuilder());
		attributeTypeQueryBuildersMap.put(MetaTypeItem.ITEM_TYPE, new PostgresRelatedItemAttributeQueryBuilder());
		attributeTypeQueryBuildersMap.put(MetaRelationTypeItem.ITEM_TYPE, new PostgresRelationAttributeQueryBuilder());
		attributeTypeQueryBuildersMap.put(MetaEnumTypeItem.ITEM_TYPE, new PostgresEnumAttributeQueryBuilder());
	}

	@Override
	public QueryBuilder<SelectItemAttributeOperationConfigContext> getAttributeTypeQueryBuilder(SelectItemAttributeOperationConfigContext context) {
		MetaAttributeTypeItem attribute = context.getOperationSpec().getAttribute();
		return attributeTypeQueryBuildersMap.computeIfAbsent(attribute.getAttributeTypeCode(), key -> {
			throw new NotFoundAttributeLoaderException(String.format("Unable to find query builder for Postgres::[%s]", key));
		});
	}
}
