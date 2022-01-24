package com.coretex.orm.core.activeorm.interceptors.impl;

import com.coretex.orm.core.activeorm.interceptors.*;
import com.coretex.items.core.GenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

@Interceptor(
		items = GenericItem.class
)
public class CommonItemLifecycleLoggerInterceptor<I extends GenericItem>
		implements
		OnLoadInterceptor<I>,
		OnRemoveInterceptor<I>,
		OnCreateInterceptor<I>,
		OnSavedInterceptor<I>,
		OnSavePrepareInterceptor<I> {
	private final Logger LOG = LoggerFactory.getLogger(CommonItemLifecycleLoggerInterceptor.class);

	@Override
	public void onCreateAction(I item) {

		log(() -> LOG.debug(format("Created item::[%s] ", item.getClass())));
	}

	@Override
	public void onLoadAction(I item) {

		log(() -> LOG.debug(format("Loaded item::[%s], uuid::[%s] ", item.getClass(), item.getUuid())));
	}

	@Override
	public void onRemoveAction(I item) {

		log(() -> LOG.debug(format("Removed item::[%s], uuid::[%s] ", item.getClass(), item.getUuid())));
	}

	@Override
	public void onSavePrepareAction(I item) {

		log(() -> {
			if(item.getItemContext().isNew()){
				LOG.debug(format("Save prepare new item::[%s] ", item.getClass()));
			}else {
				LOG.debug(format("Save prepare item::[%s], uuid::[%s] ", item.getClass(), item.getUuid()));
			}

		});
	}


	@Override
	public void onSavedAction(I item) {


		log(() -> {
			if(item.getItemContext().isNew()){
				LOG.debug(format("Saved new item::[%s], uuid::[%s] ", item.getClass(), item.getUuid()));
			}else {
				LOG.debug(format("Saved item::[%s], uuid::[%s] ", item.getClass(), item.getUuid()));
			}

		});
	}

	private void log(Runnable log) {
		if (LOG.isDebugEnabled()) {
			log.run();
		}
	}
}
