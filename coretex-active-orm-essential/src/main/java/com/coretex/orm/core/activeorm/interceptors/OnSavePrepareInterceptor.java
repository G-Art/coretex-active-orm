package com.coretex.orm.core.activeorm.interceptors;

import com.coretex.orm.meta.AbstractGenericItem;

public interface OnSavePrepareInterceptor<I extends AbstractGenericItem> {

	void onSavePrepareAction(I item);
}
