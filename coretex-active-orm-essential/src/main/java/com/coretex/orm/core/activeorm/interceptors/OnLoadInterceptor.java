package com.coretex.orm.core.activeorm.interceptors;

import com.coretex.orm.meta.AbstractGenericItem;

public interface OnLoadInterceptor<I extends AbstractGenericItem> {

	void onLoadAction(I item);
}
