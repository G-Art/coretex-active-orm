package com.coretex.orm.core.services.bootstrap.impl.initializers;

import com.coretex.orm.core.services.bootstrap.meta.MetaCollector;
import com.coretex.items.core.RegularTypeItem;
import com.google.common.collect.ImmutableTable;

public class RegularTypeInitializer implements CoretexContextInitializer<ImmutableTable<Class, String, RegularTypeItem>> {

	private final MetaCollector metaCollector;

	public RegularTypeInitializer(MetaCollector metaCollector) {
		this.metaCollector = metaCollector;
	}

	@Override
	public ImmutableTable<Class, String, RegularTypeItem> initialize() {
		ImmutableTable.Builder<Class, String, RegularTypeItem> regularTypeItemBuilder = ImmutableTable.builder();
		this.metaCollector.collectSystemRegularTypeTypes().forEach(regularTypeItem ->
				regularTypeItemBuilder.put(regularTypeItem.getRegularClass(), regularTypeItem.getDbType().toLowerCase()
						, regularTypeItem)
		);

		return regularTypeItemBuilder.build();
	}
}
