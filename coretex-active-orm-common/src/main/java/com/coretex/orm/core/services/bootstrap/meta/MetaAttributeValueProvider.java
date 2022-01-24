package com.coretex.orm.core.services.bootstrap.meta;

import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.context.provider.AttributeProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

public class MetaAttributeValueProvider implements AttributeProvider {

	private ImmutableMap<String, Object> itemState;

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue(String attributeName, ItemContext ctx) {
		if (nonNull(itemState)) {
			return (T) itemState.get(attributeName);
		}
		return null;
	}

	void init(Map<String, Object> itemValues) {
		if (itemState == null) {
			this.itemState = isNotEmpty(itemValues) ? freezeAttributes(itemValues) : ImmutableMap.of();
		}
	}

	public ImmutableMap<String, Object> getItemState() {
		return itemState;
	}

	private ImmutableMap<String, Object> freezeAttributes(Map<String, Object> itemValues) {
		Builder<String, Object> builder = ImmutableMap.builder();
		for (Map.Entry<String, Object> valueEntry : itemValues.entrySet()) {
			Object resultValue = valueEntry.getValue();
			if (resultValue instanceof Set) {
				resultValue = ImmutableSet.copyOf(((Set) resultValue));
			}
			if (resultValue instanceof List) {
				resultValue = ImmutableList.copyOf(((List) resultValue));
			}
			builder.put(valueEntry.getKey(), resultValue);
		}
		return builder.build();
	}
}
