package com.coretex.orm.core.activeorm.query.specs;

import com.coretex.orm.core.activeorm.query.operations.contexts.AbstractOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.contexts.LocalizedDataSaveOperationConfigContext;
import com.coretex.orm.core.services.bootstrap.dialect.AbstractLocalizedDataSaveQueryBuilder;
import com.coretex.items.core.MetaAttributeTypeItem;

public class LocalizedDataSaveOperationSpec extends ModificationOperationSpec {

	private MetaAttributeTypeItem attributeTypeItem;

	private AbstractLocalizedDataSaveQueryBuilder.LocalizedAttributeSaveFetcher fetcher;

	private String insertQuery;
	private String updateQuery;

	public LocalizedDataSaveOperationSpec(AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator, MetaAttributeTypeItem attributeTypeItem) {
		super(initiator.getOperationSpec().getItem());
		setNativeQuery(false);
		this.attributeTypeItem = attributeTypeItem;
	}

	public void setFetcher(AbstractLocalizedDataSaveQueryBuilder.LocalizedAttributeSaveFetcher fetcher) {
		this.fetcher = fetcher;
	}

	public void setInsertQuery(String insertQuery) {
		this.insertQuery = insertQuery;
	}

	public void setUpdateQuery(String updateQuery) {
		this.updateQuery = updateQuery;
	}

	@Override
	public void flush() {
		//ignored
	}

	public String getInsertQuery() {
		return insertQuery;
	}

	public String getUpdateQuery() {
		return updateQuery;
	}

	public AbstractLocalizedDataSaveQueryBuilder.LocalizedAttributeSaveFetcher getFetcher() {
		return fetcher;
	}

	public MetaAttributeTypeItem getAttributeTypeItem() {
		return attributeTypeItem;
	}

	@Override
	public LocalizedDataSaveOperationConfigContext createOperationContext() {
		return new LocalizedDataSaveOperationConfigContext(this);
	}

}
