package com.coretex.orm.core.activeorm.interceptors.impl;

import com.coretex.orm.core.activeorm.cache.CacheService;
import com.coretex.orm.core.activeorm.cache.impl.ItemBasedCacheInvalidationContext;
import com.coretex.orm.core.activeorm.interceptors.Interceptor;
import com.coretex.orm.core.activeorm.interceptors.OnRemoveInterceptor;
import com.coretex.orm.core.activeorm.interceptors.OnSavedInterceptor;
import com.coretex.items.core.GenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Interceptor(
		items = GenericItem.class
)
public class CacheInvalidationInterceptor<I extends GenericItem> implements
		OnRemoveInterceptor<I>,
		OnSavedInterceptor<I>{

	private final Logger LOG = LoggerFactory.getLogger(CacheInvalidationInterceptor.class);

	private final CacheService cacheService;

	public CacheInvalidationInterceptor(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	@Override
	public void onRemoveAction(I item) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("[ACTION::REMOVE] Invalidate cache for [%s::%s]", item.getMetaType().getTypeCode(), item.getUuid()));
		}
		cacheService.invalidateFor(new ItemBasedCacheInvalidationContext(item));
	}

	@Override
	public void onSavedAction(I item) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("[ACTION::SAVED] Invalidate cache for [%s::%s]", item.getMetaType().getTypeCode(), item.getUuid()));
		}
		cacheService.invalidateFor(new ItemBasedCacheInvalidationContext(item));
	}
}
