package com.coretex.orm.core.activeorm.query.specs;

import com.coretex.orm.core.activeorm.query.operations.contexts.AbstractOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.contexts.LocalizedDataRemoveOperationConfigContext;
import com.coretex.items.core.MetaAttributeTypeItem;

import java.util.HashMap;
import java.util.Map;

public class LocalizedDataRemoveOperationSpec extends ModificationOperationSpec {

	private MetaAttributeTypeItem attributeTypeItem;
	private Map<String, Object> params = new HashMap<>();


	public LocalizedDataRemoveOperationSpec(AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator, MetaAttributeTypeItem attributeTypeItem) {
		super(initiator.getOperationSpec().getItem());
		setNativeQuery(false);
		this.attributeTypeItem = attributeTypeItem;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	@Override
	public void flush() {
		//ignored
	}

	public MetaAttributeTypeItem getAttributeTypeItem() {
		return attributeTypeItem;
	}

	@Override
	public LocalizedDataRemoveOperationConfigContext createOperationContext() {
		return new LocalizedDataRemoveOperationConfigContext(this);
	}
}
