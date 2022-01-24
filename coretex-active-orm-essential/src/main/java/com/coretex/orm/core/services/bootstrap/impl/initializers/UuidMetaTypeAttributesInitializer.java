package com.coretex.orm.core.services.bootstrap.impl.initializers;

import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;

import java.util.UUID;
import java.util.function.Function;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

public class UuidMetaTypeAttributesInitializer implements CoretexContextInitializer<ImmutableMap<UUID, MetaAttributeTypeItem>> {

	private final ImmutableTable<String, String, MetaAttributeTypeItem> metaTypesAttributes;

	public UuidMetaTypeAttributesInitializer(ImmutableTable<String, String, MetaAttributeTypeItem> metaTypesAttributes) {
		this.metaTypesAttributes = metaTypesAttributes;
	}

	@Override
	public ImmutableMap<UUID, MetaAttributeTypeItem> initialize() {
		return metaTypesAttributes.values()
				.stream()
				.collect(toImmutableMap(AbstractGenericItem::getUuid, Function.identity(), (v1, v2) -> v1));
	}

}
