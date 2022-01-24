package com.coretex.orm.core.activeorm.interceptors;

import com.coretex.orm.meta.AbstractGenericItem;

public interface OnCreateInterceptor<I extends AbstractGenericItem> {

	void onCreateAction(I item);
}
