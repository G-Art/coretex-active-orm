package com.coretex.orm.core.services.bootstrap.dialect.mysql;

import com.coretex.orm.core.activeorm.query.operations.contexts.SelectItemAttributeOperationConfigContext;
import com.coretex.orm.core.services.bootstrap.dialect.AbstractItemAttributeQueryBuilder;
import com.coretex.orm.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.orm.core.services.items.exceptions.NotFoundAttributeLoaderException;
import com.coretex.items.core.*;
import com.google.common.collect.Maps;

import java.util.Map;

public class MysqlItemAttributeQueryBuilder extends AbstractItemAttributeQueryBuilder {

	private final Map<String, QueryBuilder<SelectItemAttributeOperationConfigContext>> attributeTypeQueryBuildersMap;

	public MysqlItemAttributeQueryBuilder() {
		attributeTypeQueryBuildersMap = Maps.newHashMap();
		attributeTypeQueryBuildersMap.put(RegularTypeItem.ITEM_TYPE, new MysqlRegularAttributeQueryBuilder());
		attributeTypeQueryBuildersMap.put(MetaTypeItem.ITEM_TYPE, new MysqlRelatedItemAttributeQueryBuilder());
		attributeTypeQueryBuildersMap.put(MetaRelationTypeItem.ITEM_TYPE, new MysqlRelationAttributeQueryBuilder());
		attributeTypeQueryBuildersMap.put(MetaEnumTypeItem.ITEM_TYPE, new MysqlEnumAttributeQueryBuilder());

	}

	@Override
	public QueryBuilder<SelectItemAttributeOperationConfigContext> getAttributeTypeQueryBuilder(SelectItemAttributeOperationConfigContext context) {
		MetaAttributeTypeItem attribute = context.getOperationSpec().getAttribute();
		return attributeTypeQueryBuildersMap.computeIfAbsent(attribute.getAttributeTypeCode(), key -> {
			throw new NotFoundAttributeLoaderException(String.format("Unable to find query builder for Mysql::[%s]", key));
		});
	}
}
