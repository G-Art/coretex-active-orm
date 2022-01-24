package com.coretex.orm.core.services.bootstrap.dialect.postgres;

import com.coretex.orm.core.activeorm.query.operations.contexts.LocalizedDataRemoveOperationConfigContext;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;

public class PostgresLocalizedDataRemoveQueryBuilder implements QueryBuilder<LocalizedDataRemoveOperationConfigContext> {

	private final static String DELETE_LOCALIZED_DATA_QUERY = "delete from %s_loc where owner = :owner and attribute = :attribute";


	@Override
	public String createQuery(LocalizedDataRemoveOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		GenericItem item = operationSpec.getItem();
		return String.format(DELETE_LOCALIZED_DATA_QUERY, item.getMetaType().getTableName());
	}

	@Override
	public void prepareValueData(LocalizedDataRemoveOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		GenericItem item = operationSpec.getItem();
		MetaAttributeTypeItem attributeTypeItem = operationSpec.getAttributeTypeItem();
		if(AttributeTypeUtils.isRelationAttribute(attributeTypeItem)){
			throw new IllegalArgumentException(String.format("Relation attribute cant be localized [name: %s] [owner: %s]", attributeTypeItem.getAttributeName(), attributeTypeItem.getOwner().getTypeCode()));
		}
		operationSpec.getParams().put("owner", item.getUuid());
		operationSpec.getParams().put("attribute", attributeTypeItem.getUuid());
	}
}
