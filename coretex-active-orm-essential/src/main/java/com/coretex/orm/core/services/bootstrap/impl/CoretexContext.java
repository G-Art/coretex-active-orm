package com.coretex.orm.core.services.bootstrap.impl;

import com.coretex.orm.core.activeorm.translator.TypeTranslator;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.core.services.bootstrap.impl.initializers.*;
import com.coretex.orm.core.services.bootstrap.meta.MetaCollector;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumValueTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CoretexContext implements MetaTypeProvider, DbInfoProvider {

	private final Logger LOG = LoggerFactory.getLogger(CoretexContext.class);

	private ImmutableTable<Class, String, RegularTypeItem> regularTypes;
	private ImmutableTable<Class, String, MetaEnumValueTypeItem> metaEnumValueTypes;
	private ImmutableTable<Class, UUID, MetaEnumValueTypeItem> metaEnumValueTypesUuid;

	private ImmutableMap<String, MetaTypeItem> metaTypes;
	private ImmutableMap<UUID, MetaTypeItem> uuidMetaTypes;
	private ImmutableMap<String, Set<MetaTypeItem>> metaTypesForTable;

	private ImmutableTable<String, String, MetaAttributeTypeItem> metaTypesAttributes;
	private ImmutableMap<UUID, MetaAttributeTypeItem> uuidMetaTypesAttributes;

	private MetaCollector metaCollector;
	private DbDialectService dbDialectService;

	private Map<String, TypeTranslator<?>> typeTranslatorMap;

	public CoretexContext(MetaCollector metaCollector, DbDialectService dbDialectService) {
		this.metaCollector = metaCollector;
		this.dbDialectService = dbDialectService;
		init();
	}

	private void init() {
		LOG.info("Initialize CoreTex context");
		this.metaCollector.load();

		metaTypes = new MetaTypeInitializer(metaCollector).initialize();
		regularTypes = new RegularTypeInitializer(metaCollector).initialize();
		metaEnumValueTypes = new MetaEnumValueTypesInitializer(metaCollector).initialize();

		uuidMetaTypes = new UuidMetaTypeInitializer(metaTypes).initialize();
		metaTypesForTable = new MetaTypeForTableInitializer(metaCollector).initialize();
		metaTypesAttributes = new MetaTypeAttributesInitializer(metaTypes).initialize();

		uuidMetaTypesAttributes = new UuidMetaTypeAttributesInitializer(metaTypesAttributes).initialize();
		metaEnumValueTypesUuid = new MetaEnumValueTypesUuidInitializer(metaCollector).initialize();

		typeTranslatorMap = new TypeTranslatorMapInitializer().initialize();
	}

	@Override
	public void reload() {
		init();
	}

	public MetaCollector getMetaCollector() {
		return metaCollector;
	}


	@Override
	public MetaTypeItem findMetaType(String typeCode) {
		return metaTypes.get(typeCode);
	}

	@Override
	public Set<MetaTypeItem> findMetaTypeForTable(String table) {
		return metaTypesForTable.get(table);
	}

	@Override
	public MetaTypeItem findMetaType(UUID typeCodeUUID) {
		return uuidMetaTypes.get(typeCodeUUID);
	}

	@Override
	public MetaAttributeTypeItem findMetaAttributeTypeItem(UUID typeCodeUUID) {
		return uuidMetaTypesAttributes.get(typeCodeUUID);
	}

	@Override
	public <E extends Enum> MetaEnumValueTypeItem findMetaEnumValueTypeItem(E enm) {
		return metaEnumValueTypes.get(enm.getDeclaringClass(), enm.name().toLowerCase());
	}

	@Override
	public <E extends Enum> E findMetaEnumValueTypeItem(Class clazz, UUID uuid) {
		return (E) Enum.valueOf(clazz, metaEnumValueTypesUuid.get(clazz, uuid).getValue().toUpperCase());
	}

	@Override
	public Collection<MetaTypeItem> getAllMetaTypes() {
		return metaTypes.values();
	}

	@Override
	public RegularTypeItem getRegularType(String typeCode) {
		var column = regularTypes.column(typeCode);
		if (column.isEmpty()) {
			return null;
		}
		return column.values().iterator().next();
	}

	@Override
	public RegularTypeItem getRegularType(Class clazz) {
		var row = regularTypes.row(clazz);
		if (row.isEmpty()) {
			return null;
		}
		return row.values().iterator().next();
	}

	@Override
	public Map<String, MetaAttributeTypeItem> getAllAttributes(String metaTypeCode) {
		return metaTypesAttributes.row(metaTypeCode);
	}

	@Override
	public String getSqlTypeName(RegularTypeItem regularTypeItem) {
		return dbDialectService.getSqlTypeName(regularTypeItem);
	}

	@Override
	public Integer getSqlType(RegularTypeItem regularTypeItem) {
		return dbDialectService.getSqlTypeId(getSqlTypeName(regularTypeItem));
	}

	@Override
	public TypeTranslator<?> getTypeTranslator(Class<?> typeClass) {
		String typeClassName = typeClass.getCanonicalName();
		return typeTranslatorMap.get(typeClassName);
	}

	@Override
	public DbDialectService getDbDialectService() {
		return dbDialectService;
	}
}
