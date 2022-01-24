package com.coretex.orm.core.services.bootstrap.dialect;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.items.core.RegularTypeItem;
import org.apache.calcite.sql.SqlDialect;

import java.util.Date;

public interface DbDialectService {
	Integer getSqlTypeId(String sqlTypeName);

	String dateToString(Date date);

	Date stringToDate(String date);

	QueryBuilder<? extends OperationConfigContext<?>> getQueryBuilder(QueryType queryType);

	String getSqlTypeName(RegularTypeItem regularTypeItem);

	SqlDialect.DatabaseProduct getDatabaseProduct();
}
