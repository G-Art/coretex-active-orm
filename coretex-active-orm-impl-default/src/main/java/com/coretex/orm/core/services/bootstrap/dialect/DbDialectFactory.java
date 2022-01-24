package com.coretex.orm.core.services.bootstrap.dialect;

import com.coretex.orm.core.services.bootstrap.dialect.mysql.MysqlDbDialectService;
import com.coretex.orm.core.services.bootstrap.dialect.mysql.MysqlMetaDataExtractor;
import com.coretex.orm.core.services.bootstrap.dialect.postgres.PostgresDbDialectService;
import com.coretex.orm.core.services.bootstrap.dialect.postgres.PostgresMetaDataExtractor;
import com.coretex.orm.core.services.bootstrap.meta.MetaDataExtractor;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DbDialectFactory {

	protected enum SqlDialect {
		MYSQL(MysqlDbDialectService::new, MysqlMetaDataExtractor::new),
		POSTGRES(PostgresDbDialectService::new, PostgresMetaDataExtractor::new),
		ORACLE(null, null);

		private final Function<DataSource, DbDialectService> dbDialectService;
		private final Function<DataSource, MetaDataExtractor> metaDataExtractor;

		private static final Map<String, SqlDialect> ENUM_MAP;

		static {
			Map<String, SqlDialect> map = new ConcurrentHashMap<>();
			for (SqlDialect instance : SqlDialect.values()) {
				map.put(instance.name().toLowerCase(), instance);
			}
			map.put("postgresql", SqlDialect.POSTGRES); //required to avoid misunderstanding and improve reliability
			ENUM_MAP = Collections.unmodifiableMap(map);
		}


		SqlDialect(Function<DataSource, DbDialectService> dbDialectService,
		           Function<DataSource, MetaDataExtractor> metaDataExtractor) {
			this.dbDialectService = dbDialectService;
			this.metaDataExtractor = metaDataExtractor;
		}

		public DbDialectService getDbDialectService(DataSource dataSource) {
			return Optional.ofNullable(dbDialectService)
					.orElseThrow(() -> {
						throw new UnsupportedOperationException(this.name() + " dialect is not supported yet");
					})
					.apply(dataSource);
		}

		public MetaDataExtractor getMetaDataExtractor(DataSource dataSource) {
			return Optional.ofNullable(metaDataExtractor)
					.orElseThrow(() -> {
						throw new UnsupportedOperationException(this.name() + " dialect is not supported yet");
					})
					.apply(dataSource);
		}

		public static SqlDialect get(String name) {
			return ENUM_MAP.get(name.toLowerCase());
		}
	}

	public DbDialectService getDialectService(DataSource defaultDataSource, String dialect) {
		return SqlDialect.get(dialect).getDbDialectService(defaultDataSource);
	}

	public MetaDataExtractor getMetaDataExtractor(DataSource dataSource, String dialect) {
		return SqlDialect.get(dialect).getMetaDataExtractor(dataSource);
	}

}
