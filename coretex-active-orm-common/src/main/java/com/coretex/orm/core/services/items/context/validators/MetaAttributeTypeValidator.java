package com.coretex.orm.core.services.items.context.validators;

import com.coretex.orm.core.services.items.context.ItemContext;

public interface MetaAttributeTypeValidator {
	void validate(String attributeName, ItemContext owner);
}
