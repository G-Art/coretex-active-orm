package com.coretex.orm.core.services.bootstrap.impl.initializers;

import com.coretex.orm.core.services.bootstrap.meta.MetaCollector;
import com.coretex.items.core.MetaEnumValueTypeItem;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;

import java.util.UUID;

public class MetaEnumValueTypesUuidInitializer implements CoretexContextInitializer<ImmutableTable<Class, UUID, MetaEnumValueTypeItem>> {

	private final MetaCollector metaCollector;

	public MetaEnumValueTypesUuidInitializer(MetaCollector metaCollector) {
		this.metaCollector = metaCollector;
	}

	@Override
	public ImmutableTable<Class, UUID, MetaEnumValueTypeItem> initialize() {
		Builder<Class, UUID, MetaEnumValueTypeItem> classUUIDMetaEnumValueTypeItemBuilder = ImmutableTable.builder();
		this.metaCollector.collectMetaEnumValueTypeItems().forEach(enumValue ->
				classUUIDMetaEnumValueTypeItemBuilder.put(enumValue.getOwner().getEnumClass(), enumValue.getUuid()
						, enumValue)
		);

		return classUUIDMetaEnumValueTypeItemBuilder.build();
	}
}
