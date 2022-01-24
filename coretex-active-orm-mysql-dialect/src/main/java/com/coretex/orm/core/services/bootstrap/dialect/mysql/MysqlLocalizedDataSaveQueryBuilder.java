package com.coretex.orm.core.services.bootstrap.dialect.mysql;

import com.coretex.orm.core.activeorm.query.operations.contexts.LocalizedDataSaveOperationConfigContext;
import com.coretex.orm.core.activeorm.query.specs.LocalizedDataSaveOperationSpec;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.services.bootstrap.dialect.AbstractLocalizedDataSaveQueryBuilder;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MysqlLocalizedDataSaveQueryBuilder extends AbstractLocalizedDataSaveQueryBuilder {

	private final static String INSERT_LOCALIZED_DATA_QUERY = "insert into %s_loc (owner, attribute, localeiso, value) values (UUID_TO_BIN(:owner), UUID_TO_BIN(:attribute), :localeiso, :value)";
	private final static String UPDATE_LOCALIZED_DATA_QUERY = "update %s_loc set value = :value where owner = UUID_TO_BIN(:owner) and attribute = UUID_TO_BIN(:attribute) and localeiso = :localeiso";

	public MysqlLocalizedDataSaveQueryBuilder(DbDialectService dbDialectService) {
		super(dbDialectService);
	}

	@Override
	public String createQuery(LocalizedDataSaveOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();
		return "select 'Fake query' AS fake";
	}

	@Override
	public void prepareValueData(LocalizedDataSaveOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();
		var attributeTypeItem = operationSpec.getAttributeTypeItem();
		if (AttributeTypeUtils.isRelationAttribute(attributeTypeItem)) {
			throw new IllegalArgumentException(String.format("Relation attribute cant be localized [name: %s] [owner: %s]", attributeTypeItem.getAttributeName(), attributeTypeItem.getOwner().getTypeCode()));
		}

		var fetcher = new LocalizedAttributeSaveFetcher(
				item.getItemContext().getOriginLocalizedValues(attributeTypeItem.getAttributeName()),
				item.getItemContext().getLocalizedValues(attributeTypeItem.getAttributeName())
		) {
			@Override
			public SqlParameterSource[] buildParams(Map<Locale, Object> values, LocalizedDataSaveOperationSpec operationSpec) {
				SqlParameterSource[] parameterSources = new SqlParameterSource[values.size()];
				AtomicInteger index = new AtomicInteger();
				values.forEach((locale, o) -> {
					Map<String, Object> params = new HashMap<>();
					params.put("owner", operationSpec.getItem().getUuid().toString());
					params.put("attribute", operationSpec.getAttributeTypeItem().getUuid().toString());
					params.put("localeiso", locale.toString());
					Object value = prepareValue(o, operationSpec.getAttributeTypeItem());
					if(value instanceof UUID || value instanceof Character){
						params.put("value", value.toString());
					}else {
						params.put("value", value);
					}
					parameterSources[index.getAndIncrement()] = new MapSqlParameterSource(params);
				});
				return parameterSources;
			}
		};
		operationSpec.setFetcher(fetcher);

		var insertQuery = String.format(INSERT_LOCALIZED_DATA_QUERY, attributeTypeItem.getOwner().getTableName());
		var updateQuery = String.format(UPDATE_LOCALIZED_DATA_QUERY, attributeTypeItem.getOwner().getTableName());

		operationSpec.setInsertQuery(insertQuery);
		operationSpec.setUpdateQuery(updateQuery);
	}

}
