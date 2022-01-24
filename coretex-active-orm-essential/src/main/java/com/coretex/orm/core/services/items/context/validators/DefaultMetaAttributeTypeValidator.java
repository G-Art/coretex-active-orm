package com.coretex.orm.core.services.items.context.validators;

import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.orm.core.services.bootstrap.impl.MetaTypeProvider;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.google.common.base.Preconditions;

public class DefaultMetaAttributeTypeValidator implements MetaAttributeTypeValidator {

	private final MetaTypeProvider metaTypeProvider;

	public DefaultMetaAttributeTypeValidator(MetaTypeProvider metaTypeProvider) {
		this.metaTypeProvider = metaTypeProvider;
	}

	@Override
	public void validate(String attributeName, ItemContext owner) {
		MetaAttributeTypeItem metaAttributeTypeItem = metaTypeProvider.findAttribute(owner.getTypeCode(), attributeName);
		Preconditions.checkNotNull(metaAttributeTypeItem, "Attribute ["+attributeName+"] is not exist for type ["+owner.getTypeCode()+"]");
		Preconditions.checkArgument(metaAttributeTypeItem.getLocalized(), "Attribute ["+attributeName+"] is not localized for type ["+owner.getTypeCode()+"]");

	}
}
