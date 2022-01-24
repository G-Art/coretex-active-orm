package com.coretex.orm.core.services.items.context.attributes;

public interface AttributeValueHolderState {

	void flush();

	boolean isDirty();

	boolean isLoaded();

	void refresh();
}
