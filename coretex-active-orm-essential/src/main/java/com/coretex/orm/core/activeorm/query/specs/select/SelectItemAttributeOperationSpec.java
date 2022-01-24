package com.coretex.orm.core.activeorm.query.specs.select;

import com.coretex.orm.core.activeorm.query.operations.contexts.SelectItemAttributeOperationConfigContext;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.items.core.MetaAttributeTypeItem;
import org.springframework.jdbc.core.ResultSetExtractor;

public class SelectItemAttributeOperationSpec extends SelectOperationSpec {

	private MetaAttributeTypeItem attribute;
	private ItemContext ctx;
	private DbDialectService dbDialectService;
	private SelectItemAttributeOperationConfigContext itemAttributeOperationConfigContext;

	public SelectItemAttributeOperationSpec(MetaAttributeTypeItem attribute, ItemContext ctx, DbDialectService dbDialectService) {
		super();
		this.attribute = attribute;
		this.ctx = ctx;
		this.dbDialectService = dbDialectService;
		this.itemAttributeOperationConfigContext = new SelectItemAttributeOperationConfigContext(this);
	}

	public SelectItemAttributeOperationSpec(MetaAttributeTypeItem attribute, ItemContext ctx, DbDialectService dbDialectService, ResultSetExtractor<?> customExtractor) {
		this(attribute, ctx, dbDialectService);
		setCustomExtractor(customExtractor);
	}


	@Override
	public String getQuery() {
		return getQueryBuilder(itemAttributeOperationConfigContext).buildQuery(itemAttributeOperationConfigContext);
	}

	protected QueryBuilder<SelectItemAttributeOperationConfigContext> getQueryBuilder(SelectItemAttributeOperationConfigContext operationConfigContext){
		return  (QueryBuilder<SelectItemAttributeOperationConfigContext>) dbDialectService.getQueryBuilder(operationConfigContext.getQueryType());
	}

	@Override
	public SelectItemAttributeOperationConfigContext createOperationContext() {
		return itemAttributeOperationConfigContext;
	}

	public MetaAttributeTypeItem getAttribute() {
		return attribute;
	}

	public ItemContext getCtx() {
		return ctx;
	}

	@Override
	public String toString() {
		return "query: [" + this.getQuery() + "], query parameters: [" + this.getParameters() + "]";
	}

}
