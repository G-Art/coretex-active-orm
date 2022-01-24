package com.coretex.orm.core.services.items.context.provider.strategies;

import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.items.core.MetaAttributeTypeItem;

public interface LoadAttributeValueStrategy {

	Object load(ItemContext ctx, MetaAttributeTypeItem attribute);
}
