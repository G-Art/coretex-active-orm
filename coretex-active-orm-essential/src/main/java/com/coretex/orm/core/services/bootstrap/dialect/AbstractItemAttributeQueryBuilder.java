package com.coretex.orm.core.services.bootstrap.dialect;

import com.coretex.orm.core.activeorm.query.operations.contexts.SelectItemAttributeOperationConfigContext;

public abstract class AbstractItemAttributeQueryBuilder implements QueryBuilder<SelectItemAttributeOperationConfigContext>{

	public AbstractItemAttributeQueryBuilder() {
	}

	public abstract QueryBuilder<SelectItemAttributeOperationConfigContext> getAttributeTypeQueryBuilder(SelectItemAttributeOperationConfigContext context);

	@Override
	public String createQuery(SelectItemAttributeOperationConfigContext context) {
		return getAttributeTypeQueryBuilder(context).createQuery(context);
	}

	@Override
	public void prepareValueData(SelectItemAttributeOperationConfigContext context) {
		getAttributeTypeQueryBuilder(context).prepareValueData(context);
	}
}
