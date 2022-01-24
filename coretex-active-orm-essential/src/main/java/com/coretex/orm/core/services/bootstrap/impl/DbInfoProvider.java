package com.coretex.orm.core.services.bootstrap.impl;

import com.coretex.orm.core.activeorm.translator.TypeTranslator;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;

public interface DbInfoProvider {


	TypeTranslator<?> getTypeTranslator(Class<?> typeClass);

	DbDialectService getDbDialectService();
}
