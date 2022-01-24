package com.coretex.orm.core.services.bootstrap.dialect;

import com.coretex.orm.core.activeorm.query.operations.contexts.LocalizedDataSaveOperationConfigContext;
import com.coretex.orm.core.activeorm.query.specs.LocalizedDataSaveOperationSpec;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractLocalizedDataSaveQueryBuilder implements QueryBuilder<LocalizedDataSaveOperationConfigContext> {

	private DbDialectService dbDialectService;

	public AbstractLocalizedDataSaveQueryBuilder(DbDialectService dbDialectService) {
		this.dbDialectService = dbDialectService;
	}

	public DbDialectService getDbDialectService() {
		return dbDialectService;
	}

	protected Object prepareValue(Object o, MetaAttributeTypeItem attribute) {
		if(AttributeTypeUtils.isRegularTypeAttribute(attribute)){
			Class regularClass = AttributeTypeUtils.getItemClass(attribute);
			if(regularClass != null && regularClass.isAssignableFrom(Class.class)){
				return ((Class)o).getName();
			}
			if(regularClass != null && regularClass.equals(Date.class)){
				return getDbDialectService().dateToString(new java.sql.Timestamp(((Date)o).getTime()));
			}
		}
		return o;
	}

	public abstract class  LocalizedAttributeSaveFetcher {

		private Map<Locale, Object> update;
		private Map<Locale, Object> insert;

		private Map<Locale, Object> originLocalizedValues;
		private Map<Locale, Object> localizedValues;

		public LocalizedAttributeSaveFetcher(Map<Locale, Object> originLocalizedValues, Map<Locale, Object> localizedValues) {
			this.originLocalizedValues = Optional.ofNullable(originLocalizedValues).orElseGet(Maps::newHashMap);
			this.localizedValues = Optional.ofNullable(localizedValues).orElseGet(Maps::newHashMap);
		}

		public boolean hasValuesForUpdate(){
			if(update == null){
				update = localizedValues.entrySet()
						.stream()
						.filter(entry -> {
							if(originLocalizedValues.containsKey(entry.getKey())){
								return !originLocalizedValues.get(entry.getKey()).equals(entry.getKey());
							}
							return false;
						}).collect(
								Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
						);

			}

			return !update.isEmpty();
		}

		public boolean hasValuesForInsert(){
			if(insert == null){
				insert = localizedValues.entrySet()
						.stream()
						.filter(entry -> !originLocalizedValues.containsKey(entry.getKey())).collect(
								Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
						);
			}

			return !insert.isEmpty();
		}


		public Map<Locale, Object> getUpdateValues(){
			return update;
		}

		public Map<Locale, Object> getInsertValues(){
			return insert;
		}

		public abstract SqlParameterSource[] buildParams(Map<Locale, Object> values, LocalizedDataSaveOperationSpec operationSpec);
	}
}
