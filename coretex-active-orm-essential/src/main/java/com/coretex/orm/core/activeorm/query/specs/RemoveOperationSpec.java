package com.coretex.orm.core.activeorm.query.specs;

import com.coretex.orm.core.activeorm.query.operations.contexts.RemoveOperationConfigContext;
import com.coretex.items.core.GenericItem;

public class RemoveOperationSpec extends ModificationOperationSpec {

	public RemoveOperationSpec(GenericItem item) {
		super(item);
	}

	public RemoveOperationSpec(GenericItem item, boolean cascade) {
		super(item, cascade);
	}

	public RemoveOperationSpec(GenericItem item, boolean cascade, boolean transactional) {
		super(item, cascade, transactional);
	}

	@Override
	public RemoveOperationConfigContext createOperationContext() {
		return new RemoveOperationConfigContext(this);
	}
}
