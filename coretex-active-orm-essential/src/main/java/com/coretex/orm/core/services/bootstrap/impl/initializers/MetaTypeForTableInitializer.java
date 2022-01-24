package com.coretex.orm.core.services.bootstrap.impl.initializers;

import com.coretex.orm.core.services.bootstrap.meta.MetaCollector;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.util.Set;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

public class MetaTypeForTableInitializer implements CoretexContextInitializer<ImmutableMap<String, Set<MetaTypeItem>>> {

	private final MetaCollector metaCollector;

	public MetaTypeForTableInitializer(MetaCollector metaCollector) {
		this.metaCollector = metaCollector;
	}

	@Override
	public ImmutableMap<String, Set<MetaTypeItem>> initialize() {
		return this.metaCollector.collectSystemMetaTypes().stream()
				.collect(toImmutableMap(MetaTypeItem::getTableName,
						Set::of,
						(o, o2) -> Sets.union(o, o2).immutableCopy()));
	}
}
