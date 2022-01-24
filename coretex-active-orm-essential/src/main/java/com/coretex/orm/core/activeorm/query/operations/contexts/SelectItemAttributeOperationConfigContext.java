package com.coretex.orm.core.activeorm.query.operations.contexts;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.specs.select.SelectItemAttributeOperationSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectItemAttributeOperationConfigContext extends SelectOperationConfigContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectItemAttributeOperationConfigContext.class);


	public SelectItemAttributeOperationConfigContext(SelectItemAttributeOperationSpec operationSpec){
		super(operationSpec, false);
	}

	@Override
	public SelectItemAttributeOperationSpec getOperationSpec() {
		return (SelectItemAttributeOperationSpec) super.getOperationSpec();
	}


	@Override
	public QueryType getQueryType() {
		return QueryType.SELECT_ITEM_ATTRIBUTE;
	}


}
