package com.coretex.orm.core.activeorm.interceptors;

import com.coretex.orm.meta.AbstractGenericItem;

public interface OnRemoveInterceptor<I extends AbstractGenericItem> {

	void onRemoveAction(I item);
}
