package com.coretex.orm;

import com.coretex.orm.core.CoretexConfiguration;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectFactory;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.orm.core.services.bootstrap.meta.MetaCollector;

import javax.sql.DataSource;

public class CoretexManagerBuilder {

	private final CoretexConfiguration coretexConfiguration;

	private DataSource dataSource;
	private String dialect;

	private final DbDialectFactory dialectFactory = new DbDialectFactory();

	public CoretexManagerBuilder(CoretexConfiguration coretexConfiguration, DataSource dataSource) {
		this.coretexConfiguration = coretexConfiguration;
		this.dataSource = dataSource;
		this.dialect = coretexConfiguration.getConfig("db.dialect", String.class)
				.orElse(null);
	}

	public CoretexManagerBuilder withDialect(String dialect) {
		this.dialect = dialect;
		return this;
	}

	public CoretexManagerBuilder withDataSource(DataSource dataSource){
		this.dataSource = dataSource;
		return this;
	}

	public CoretexManager build() {
		DbDialectService dialectService = dialectFactory.getDialectService(dataSource, dialect);
		MetaCollector collector = new MetaCollector(dialectFactory.getMetaDataExtractor(dataSource, dialect));
		return new CoretexManager(
				coretexConfiguration,
				dialectService,
				new CoretexContext(collector, dialectService)
		);
	}
}
