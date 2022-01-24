package com.coretex.orm.core.activeorm.services;

import com.coretex.orm.core.activeorm.interceptors.*;

import java.util.Collection;

public interface ItemInterceptorsRegistry  {

	Collection<OnCreateInterceptor> getCreateInterceptors(String type);

	Collection<OnLoadInterceptor> getLoadInterceptors(String type);

	Collection<OnRemoveInterceptor> getRemoveInterceptors(String type);

	Collection<OnSavePrepareInterceptor> getSavePrepareInterceptors(String type);

	Collection<OnSavedInterceptor> getSavedInterceptors(String type);
}
