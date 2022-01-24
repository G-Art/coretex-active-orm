package com.coretex.orm.core.services.bootstrap.dialect.postgres;

import com.coretex.items.core.*;
import com.coretex.orm.core.services.bootstrap.AbstractMetaDataExtractor;
import com.coretex.orm.meta.AbstractGenericItem;
import com.coretex.relations.core.MetaAttributeOwnerRelation;
import com.coretex.relations.core.MetaEnumValueOwnerRelation;
import com.coretex.relations.core.MetaTypeInheritanceRelation;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class PostgresMetaDataExtractor extends AbstractMetaDataExtractor {

	public PostgresMetaDataExtractor(DataSource dataSource) {
		super(dataSource);
	}

	public List<Map<String, Object>> selectTypeItems() {
		String typeAlias = "meta";
		String inhRefAlias = "mtir";
		String sql = "SELECT " +
				selectField(typeAlias, MetaTypeItem.UUID) + ", " +
				selectField(typeAlias, MetaTypeItem.TYPE_CODE) + ", " +
				selectField(typeAlias, MetaTypeItem.TABLE_NAME) + ", " +
				selectField(typeAlias, MetaTypeItem.ITEM_CLASS) + ", " +
				selectField(typeAlias, MetaTypeItem.TABLE_OWNER) + ", " +
				selectField(typeAlias, MetaTypeItem.DESCRIPTION) + ", " +
				selectField(typeAlias, MetaTypeItem.CREATE_DATE) + ", " +
				selectField(typeAlias, MetaTypeItem.UPDATE_DATE) + ", " +
				selectField(typeAlias, MetaTypeItem.ABSTRACT) + ", " +
				selectField(typeAlias, MetaRelationTypeItem.SOURCE_ATTRIBUTE) + ", " +
				selectField(typeAlias, MetaRelationTypeItem.TARGET_ATTRIBUTE) + ", " +
				selectField(typeAlias, MetaRelationTypeItem.SOURCE_TYPE) + ", " +
				selectField(typeAlias, MetaRelationTypeItem.TARGET_TYPE) + ", " +
				selectField(typeAlias, GenericItem.META_TYPE) + ", " +
				selectField(inhRefAlias, MetaTypeInheritanceRelation.TARGET, MetaTypeItem.PARENT) +
				" FROM " + fromTable(MetaTypeItem.ITEM_TYPE, typeAlias) +
				" LEFT JOIN " + fromTable(MetaTypeInheritanceRelation.ITEM_TYPE, inhRefAlias) + " ON " +
				onField(typeAlias, AbstractGenericItem.UUID) + " = " + onField(inhRefAlias, MetaTypeInheritanceRelation.SOURCE);
		return execute(sql);
	}

	public List<Map<String, Object>> selectAttributeItems() {
		String attrAlias = "attr";
		String ownerRelAlieas = "maor";
		String sql = "SELECT " +
				selectField(attrAlias, MetaAttributeTypeItem.UUID) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.ATTRIBUTE_NAME) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.COLUMN_NAME) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.CREATE_DATE) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.UPDATE_DATE) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.LOCALIZED) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.ASSOCIATED) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.UNIQUE) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.SOURCE) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.OPTIONAL) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.DEFAULT_VALUE) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.DESCRIPTION) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.ATTRIBUTE_TYPE) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.ATTRIBUTE_TYPE_CODE) + ", " +
				selectField(attrAlias, MetaAttributeTypeItem.CONTAINER_TYPE) + ", " +
				selectField(attrAlias, GenericItem.META_TYPE) + ", " +
				selectField(ownerRelAlieas, MetaAttributeOwnerRelation.TARGET, MetaAttributeTypeItem.OWNER) +
				" FROM " + fromTable(MetaAttributeTypeItem.ITEM_TYPE, attrAlias) +
				" LEFT JOIN " + fromTable(MetaAttributeOwnerRelation.ITEM_TYPE, ownerRelAlieas) + " ON " +
				onField(attrAlias, AbstractGenericItem.UUID) + " = " + onField(ownerRelAlieas, MetaAttributeOwnerRelation.SOURCE);
		return execute(sql);
	}

	public List<Map<String, Object>> selectRegularItems() {
		String regularAlias = "reg";
		String sql = "SELECT " +
				selectField(regularAlias, RegularTypeItem.UUID) + ", " +
				selectField(regularAlias, RegularTypeItem.COLUMN_SIZE) + ", " +
				selectField(regularAlias, RegularTypeItem.DB_TYPE) + ", " +
				selectField(regularAlias, RegularTypeItem.PERSISTENCE_TYPE) + ", " +
				selectField(regularAlias, RegularTypeItem.REGULAR_CLASS) + ", " +
				selectField(regularAlias, RegularTypeItem.REGULAR_ITEM_CODE) + ", " +
				selectField(regularAlias, RegularTypeItem.CREATE_DATE) + ", " +
				selectField(regularAlias, RegularTypeItem.UPDATE_DATE) + ", " +
				selectField(regularAlias, GenericItem.META_TYPE) +
				" FROM " + fromTable(RegularTypeItem.ITEM_TYPE, regularAlias);
		return execute(sql);
	}

	public List<Map<String, Object>> selectEnumTypes() {
		String enumRef = "enu";
		String sql = "SELECT " +
				selectField(enumRef, AbstractGenericItem.UUID) + ", " +
				selectField(enumRef, MetaEnumTypeItem.ENUM_CODE) + ", " +
				selectField(enumRef, MetaEnumTypeItem.ENUM_CLASS) + ", " +
				selectField(enumRef, MetaEnumTypeItem.CREATE_DATE) + ", " +
				selectField(enumRef, MetaEnumTypeItem.UPDATE_DATE) + ", " +
				selectField(enumRef, GenericItem.META_TYPE) +
				" FROM " + fromTable(MetaEnumTypeItem.ITEM_TYPE, enumRef);
		return execute(sql);
	}

	public List<Map<String, Object>> selectEnumValues() {
		String enumValRef = "enuval";
		String ownerRef = "enuowner";
		String sql = "SELECT " +
				selectField(enumValRef, AbstractGenericItem.UUID) + ", " +
				selectField(enumValRef, MetaEnumValueTypeItem.CODE) + ", " +
				selectField(enumValRef, MetaEnumValueTypeItem.VALUE) + ", " +
				selectField(enumValRef, MetaEnumValueTypeItem.CREATE_DATE) + ", " +
				selectField(enumValRef, MetaEnumValueTypeItem.UPDATE_DATE) + ", " +
				selectField(enumValRef, GenericItem.META_TYPE) + ", " +
				selectField(ownerRef, MetaEnumValueOwnerRelation.TARGET, MetaEnumValueTypeItem.OWNER) +
				" FROM " + fromTable(MetaEnumValueTypeItem.ITEM_TYPE, enumValRef) +
				" LEFT JOIN " + fromTable(MetaEnumValueOwnerRelation.ITEM_TYPE, ownerRef) + " ON " +
				onField(enumValRef, AbstractGenericItem.UUID) + " = " + onField(ownerRef, MetaEnumValueOwnerRelation.SOURCE);
		return execute(sql);
	}


//    public NamedParameterJdbcTemplate getJdbcTemplate() {
//        return jdbcTemplate;
//    }

//    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

	private String fromTable(String tableName, String alias) {
		return '"' + wrapTable(tableName.toLowerCase()) + "\" as " + alias;
	}

	private String wrapTable(String table) {
		return "t_" + table;//todo: make prefix it configurable
	}

	private String onField(String tableAlias, String attributeName) {
		return tableAlias + '.' + getColumn(attributeName);
	}

	private String selectField(String tableAlias, String attributeName) {
		return selectField(tableAlias, attributeName, attributeName);
	}

	private String selectField(String tableAlias, String attributeName, String alias) {
		return tableAlias + '.' + getColumn(attributeName) + " as \"" + alias + '"';
	}

	private String getColumn(String attributeName) {
		return '"' + (attributeName.equals(AbstractGenericItem.UUID) ? attributeName : wrapColumn(attributeName)).toLowerCase() + '"';
	}

	private String wrapColumn(String attributeName) {
		return "c_" + attributeName;
	}

}
