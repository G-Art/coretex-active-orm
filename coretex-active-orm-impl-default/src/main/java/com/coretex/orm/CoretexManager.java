package com.coretex.orm;

import com.coretex.orm.core.CoretexConfiguration;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;

public class CoretexManager {

	private final DbDialectService dialectService;


	CoretexManager(CoretexConfiguration coretexConfiguration, DbDialectService dialectService, CoretexContext collector) {
		this.dialectService = dialectService;
	}

	public DbDialectService getDialectService() {
		return dialectService;
	}
}
