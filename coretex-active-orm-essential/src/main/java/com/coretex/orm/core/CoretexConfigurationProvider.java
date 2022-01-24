package com.coretex.orm.core;

import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.core.services.bootstrap.impl.MetaTypeProvider;

public abstract class CoretexConfigurationProvider {
	protected static CoretexConfiguration coretexConfiguration;
	protected static MetaTypeProvider metaTypeProvider;
	protected static DbDialectService dbDialectService;

	public CoretexConfigurationProvider() {
		init();
	}

	protected void init() {
		coretexConfiguration = initConfiguration();
		metaTypeProvider = initMetaTypeProvider();
		dbDialectService = initDbDialectService();
	}

	protected abstract DbDialectService initDbDialectService();

	protected abstract MetaTypeProvider initMetaTypeProvider();

	protected abstract CoretexConfiguration initConfiguration();

	public static CoretexConfiguration getConfiguration() {
		if (coretexConfiguration == null) {
			throw new IllegalStateException("CoretexConfigurationProvider instance is not initialized");
		}
		return coretexConfiguration;
	}

	public static MetaTypeProvider getMetaTypeProvider() {
		if (metaTypeProvider == null) {
			throw new IllegalStateException("CoretexConfigurationProvider instance is not initialized");
		}
		return metaTypeProvider;
	}

	public static DbDialectService getDbDialectService() {
		if (dbDialectService == null) {
			throw new IllegalStateException("CoretexConfigurationProvider instance is not initialized");
		}
		return dbDialectService;
	}
}
