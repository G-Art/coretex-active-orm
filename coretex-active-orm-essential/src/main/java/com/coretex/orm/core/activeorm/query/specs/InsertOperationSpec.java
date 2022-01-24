package com.coretex.orm.core.activeorm.query.specs;

import com.coretex.orm.core.activeorm.query.operations.contexts.InsertOperationConfigContext;
import com.coretex.items.core.GenericItem;

import java.util.Objects;
import java.util.UUID;

public class InsertOperationSpec extends ModificationOperationSpec {

	private UUID newUuid;

	public InsertOperationSpec(GenericItem item) {
		super(item);
	}

	public InsertOperationSpec(GenericItem item, boolean cascade) {
		super(item, cascade);
	}

	public InsertOperationSpec(GenericItem item, boolean cascade, boolean transactional) {
		super(item, cascade, transactional);
	}

	public UUID getNewUuid() {
		if(Objects.isNull(newUuid)){
			newUuid = UUID.randomUUID();
		}
		return newUuid;
	}


	@Override
	public InsertOperationConfigContext createOperationContext() {
		return new InsertOperationConfigContext(this);
	}
}
