package com.coretex.orm.core.activeorm.interceptors;

import com.coretex.orm.meta.AbstractGenericItem;

public interface OnSavedInterceptor<I extends AbstractGenericItem> {

	void onSavedAction(I item);
}
