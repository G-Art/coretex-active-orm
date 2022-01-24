package com.coretex.orm.core;

import com.coretex.orm.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.orm.core.services.items.context.factory.ItemContextFactory;
import com.coretex.orm.core.services.items.context.provider.AttributeProvider;
import com.coretex.orm.core.services.items.context.validators.MetaAttributeTypeValidator;

public abstract class AbstractCoretexContextProvider {

	protected static AbstractCoretexContextProvider instance;

	protected abstract void init();

	private AbstractCoretexContextProvider() {
	}

	public static AbstractCoretexContextProvider getInstance(){
		if(instance == null){
			throw new IllegalStateException("CoretexContextProvider instance is not initialized");
		}
		return instance;
	}

	public abstract ItemContextFactory getItemContextFactory();
	public abstract ItemOperationInterceptorService getItemOperationInterceptorService();
	public abstract MetaAttributeTypeValidator getMetaAttributeTypeValidator();
	public abstract AttributeProvider getDefaultAttributeProvider();
}
