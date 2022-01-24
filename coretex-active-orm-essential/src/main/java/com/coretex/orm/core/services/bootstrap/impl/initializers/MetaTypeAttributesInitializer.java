package com.coretex.orm.core.services.bootstrap.impl.initializers;

import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class MetaTypeAttributesInitializer implements CoretexContextInitializer<ImmutableTable<String, String, MetaAttributeTypeItem>> {

	private final ImmutableMap<String, MetaTypeItem> metaTypes;

	public MetaTypeAttributesInitializer(ImmutableMap<String, MetaTypeItem> metaTypes) {
		this.metaTypes = metaTypes;
	}

	@Override
	public ImmutableTable<String, String, MetaAttributeTypeItem> initialize() {
		Builder<String, String, MetaAttributeTypeItem> builder = ImmutableTable.builder();
		for (Map.Entry<String, MetaTypeItem> metaTypeEntry : metaTypes.entrySet()) {
			String metaTypeCode = metaTypeEntry.getKey();
			MetaTypeItem metaType = metaTypeEntry.getValue();
			collectAttributes(new ArrayList<>(), metaType).forEach(attributeType ->
					builder.put(metaTypeCode, attributeType.getAttributeName(), attributeType));
		}
		return  builder.build();
	}

	private List<MetaAttributeTypeItem> collectAttributes(List<com.coretex.items.core.MetaAttributeTypeItem> typeAttributes, MetaTypeItem metaType) {
		if (nonNull(metaType) && isNotEmpty(metaType.getItemAttributes())) {
			typeAttributes.addAll(metaType.getItemAttributes());
		}
		if (nonNull(metaType) && nonNull(metaType.getParent())) {
			collectAttributes(typeAttributes, metaType.getParent());
		}
		return typeAttributes;
	}
}
