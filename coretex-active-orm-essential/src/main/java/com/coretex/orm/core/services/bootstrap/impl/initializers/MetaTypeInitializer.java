package com.coretex.orm.core.services.bootstrap.impl.initializers;

import com.coretex.orm.core.services.bootstrap.meta.MetaCollector;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.ImmutableMap;

import java.util.function.Function;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

public class MetaTypeInitializer implements CoretexContextInitializer<ImmutableMap<String, MetaTypeItem>> {

	private final MetaCollector metaCollector;

	public MetaTypeInitializer(MetaCollector metaCollector) {
		this.metaCollector = metaCollector;
	}

	@Override
	public ImmutableMap<String, MetaTypeItem> initialize() {
		return this.metaCollector.collectSystemMetaTypes().stream()
				.collect(toImmutableMap(MetaTypeItem::getTypeCode, Function.identity()));
	}
}
