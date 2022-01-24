package com.coretex.orm.core.services.bootstrap.dialect.postgres;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.items.core.RegularTypeItem;
import com.google.common.collect.Maps;
import org.apache.calcite.sql.SqlDialect;
import org.postgresql.core.BaseConnection;
import org.postgresql.core.TypeInfo;
import org.postgresql.jdbc.TimestampUtils;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

public class PostgresDbDialectService implements DbDialectService {

	private DataSource dataSource;
	private TypeInfo typeInfo;
	private Supplier<BaseConnection> connectionSupplier;
	private TimestampUtils timestampUtils;

	private final Map<QueryType, QueryBuilder<? extends OperationConfigContext<?>>> queryBuilders = Maps.newHashMap();

	public PostgresDbDialectService(DataSource defaultDataSource) {
		this.dataSource = defaultDataSource;
		connectionSupplier = () -> {
			try {
				return (BaseConnection) dataSource.getConnection();
			} catch (SQLException e) {
				throw new CannotGetJdbcConnectionException("Unable create connection", e);
			}
		};

		this.typeInfo = connectionSupplier.get().getTypeInfo();
		this.timestampUtils = connectionSupplier.get().getTimestampUtils();

		queryBuilders.put(QueryType.SELECT_ITEM_ATTRIBUTE, new PostgresItemAttributeQueryBuilder());
		queryBuilders.put(QueryType.INSERT, new PostgresInsertQueryBuilder());
		queryBuilders.put(QueryType.UPDATE, new PostgresUpdateQueryBuilder());
		queryBuilders.put(QueryType.DELETE, new PostgresRemoveQueryBuilder());
		queryBuilders.put(QueryType.INSERT_CASCADE, new PostgresInsertQueryBuilder());
		queryBuilders.put(QueryType.UPDATE_CASCADE, new PostgresUpdateQueryBuilder());
		queryBuilders.put(QueryType.DELETE_CASCADE, new PostgresRemoveQueryBuilder());
		queryBuilders.put(QueryType.LOCALIZED_DATA_DELETE, new PostgresLocalizedDataRemoveQueryBuilder());
		queryBuilders.put(QueryType.LOCALIZED_DATA_SAVE, new PostgresLocalizedDataSaveQueryBuilder(this));
	}

	@Override
	public Integer getSqlTypeId(String typeName) {
		try {
			return typeInfo.getSQLType(typeName.toLowerCase());
		} catch (SQLException e) {
			throw new IllegalArgumentException(String.format("Type [%s] is not available for postgres dialect", typeName), e);
		}
	}

	@Override
	public String dateToString(Date date) {
		return timestampUtils.timeToString(date, true);
	}

	@Override
	public Date stringToDate(String date){
		try {
			return timestampUtils.toTimestamp(Calendar.getInstance(), date);
		} catch (SQLException e) {
			throw new IllegalArgumentException(String.format("Parse date exception [%s]", date), e);
		}
	}

	@Override
	public QueryBuilder<? extends OperationConfigContext<?>> getQueryBuilder(QueryType queryType) {
		return queryBuilders.get(queryType);
	}

	@Override
	public String getSqlTypeName(RegularTypeItem regularTypeItem) {
		return regularTypeItem.getDbType();
	}

	@Override
	public SqlDialect.DatabaseProduct getDatabaseProduct() {
		return SqlDialect.DatabaseProduct.POSTGRESQL;
	}

}
