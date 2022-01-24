package com.coretex.orm.core.services.bootstrap.dialect.mysql;

import com.coretex.orm.core.CoretexConfigurationProvider;
import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.items.core.RegularTypeItem;
import com.google.common.collect.Maps;
import com.mysql.cj.MysqlType;
import org.apache.calcite.sql.SqlDialect;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.sql.DataSource;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class MysqlDbDialectService implements DbDialectService {

	private DataSource dataSource;//not required

	private final Map<QueryType, QueryBuilder<? extends OperationConfigContext<?>>> queryBuilders = Maps.newHashMap();

	private final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	public MysqlDbDialectService(DataSource defaultDataSource) {
		this.dataSource = defaultDataSource;

		queryBuilders.put(QueryType.SELECT_ITEM_ATTRIBUTE, new MysqlItemAttributeQueryBuilder());
		queryBuilders.put(QueryType.INSERT, new MysqlInsertQueryBuilder());
		queryBuilders.put(QueryType.UPDATE, new MysqlUpdateQueryBuilder());
		queryBuilders.put(QueryType.DELETE, new MysqlRemoveQueryBuilder());
		queryBuilders.put(QueryType.INSERT_CASCADE, new MysqlInsertQueryBuilder());
		queryBuilders.put(QueryType.UPDATE_CASCADE, new MysqlUpdateQueryBuilder());
		queryBuilders.put(QueryType.DELETE_CASCADE, new MysqlRemoveQueryBuilder());
		queryBuilders.put(QueryType.LOCALIZED_DATA_DELETE, new MysqlLocalizedDataRemoveQueryBuilder());
		queryBuilders.put(QueryType.LOCALIZED_DATA_SAVE, new MysqlLocalizedDataSaveQueryBuilder(this));
	}

	@Override
	public Integer getSqlTypeId(String typeName) {
		return MysqlType.getByName(typeName).getJdbcType();
	}

	@Override
	public String dateToString(Date date) {
		return DateFormatUtils.format(date, DATE_FORMAT_PATTERN);
	}

	@Override
	public Date stringToDate(String date) {
		try {
			return DateUtils.parseDate(date, DATE_FORMAT_PATTERN);
		} catch (ParseException e) {
			throw new IllegalArgumentException(String.format("Parse date exception [%s]", date), e);
		}
	}

	@Override
	public QueryBuilder<? extends OperationConfigContext<?>> getQueryBuilder(QueryType queryType) {
		return queryBuilders.get(queryType);
	}

	@Override
	public String getSqlTypeName(RegularTypeItem regularTypeItem) {
		if (regularTypeItem.getRegularClass().isAssignableFrom(UUID.class)) {
			return getSqlTypeName(CoretexConfigurationProvider.getMetaTypeProvider().getRegularType(String.class));
		}
		return regularTypeItem.getDbType();
	}

	@Override
	public SqlDialect.DatabaseProduct getDatabaseProduct() {
		return SqlDialect.DatabaseProduct.MYSQL;
	}

}
