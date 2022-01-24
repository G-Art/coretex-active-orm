package com.coretex.orm.core.activeorm.services.impl;

import com.coretex.orm.core.activeorm.services.ItemInterceptorsRegistry;
import com.coretex.orm.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.orm.meta.AbstractGenericItem;


public class DefaultItemOperationInterceptorService implements ItemOperationInterceptorService {

	private final ItemInterceptorsRegistry itemInterceptorsRegistry;

	public DefaultItemOperationInterceptorService(ItemInterceptorsRegistry itemInterceptorsRegistry) {
		this.itemInterceptorsRegistry = itemInterceptorsRegistry;
	}

	@Override
	public void onCreate(AbstractGenericItem item) {
		itemInterceptorsRegistry.getCreateInterceptors(item.getItemContext().getTypeCode())
				.forEach(interceptor -> interceptor.onCreateAction(item));
	}

	@Override
	public void onSavePrepare(AbstractGenericItem item) {
		itemInterceptorsRegistry.getSavePrepareInterceptors(item.getItemContext().getTypeCode())
				.forEach(interceptor -> interceptor.onSavePrepareAction(item));
	}

	@Override
	public void onSaved(AbstractGenericItem item) {
		itemInterceptorsRegistry.getSavedInterceptors(item.getItemContext().getTypeCode())
				.forEach(interceptor -> interceptor.onSavedAction(item));
	}

	@Override
	public void onRemove(AbstractGenericItem item) {
		itemInterceptorsRegistry.getRemoveInterceptors(item.getItemContext().getTypeCode())
				.forEach(interceptor -> interceptor.onRemoveAction(item));
	}

	@Override
	public void onLoad(AbstractGenericItem item) {
		itemInterceptorsRegistry.getLoadInterceptors(item.getItemContext().getTypeCode())
				.forEach(interceptor -> interceptor.onLoadAction(item));
	}
}
