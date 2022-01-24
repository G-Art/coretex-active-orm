package com.coretex.orm.core.services.bootstrap.impl.initializers;

import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.UUID;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

public class UuidMetaTypeInitializer implements CoretexContextInitializer<ImmutableMap<UUID, MetaTypeItem>> {

	private final ImmutableMap<String, MetaTypeItem> metaTypes;

	public UuidMetaTypeInitializer(ImmutableMap<String, MetaTypeItem> metaTypes) {
		this.metaTypes = metaTypes;
	}

	@Override
	public ImmutableMap<UUID, MetaTypeItem> initialize() {
		return metaTypes.entrySet().stream()
				.collect(toImmutableMap(entry -> entry.getValue().getUuid(), Map.Entry::getValue));
	}
}
