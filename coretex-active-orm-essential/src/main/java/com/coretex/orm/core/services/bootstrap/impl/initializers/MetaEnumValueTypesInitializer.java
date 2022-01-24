package com.coretex.orm.core.services.bootstrap.impl.initializers;

import com.coretex.orm.core.services.bootstrap.meta.MetaCollector;
import com.coretex.items.core.MetaEnumValueTypeItem;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;

public class MetaEnumValueTypesInitializer implements CoretexContextInitializer<ImmutableTable<Class, String, MetaEnumValueTypeItem>> {

	private final MetaCollector metaCollector;

	public MetaEnumValueTypesInitializer(MetaCollector metaCollector) {
		this.metaCollector = metaCollector;
	}

	@Override
	public ImmutableTable<Class, String, MetaEnumValueTypeItem> initialize() {
		Builder<Class, String, MetaEnumValueTypeItem> metaEnumValueTypesItemBuilder = ImmutableTable.builder();
		this.metaCollector.collectMetaEnumValueTypeItems().forEach(enumValue ->
				metaEnumValueTypesItemBuilder.put(enumValue.getOwner().getEnumClass(), enumValue.getValue()
						, enumValue)
		);

		return metaEnumValueTypesItemBuilder.build();
	}
}
