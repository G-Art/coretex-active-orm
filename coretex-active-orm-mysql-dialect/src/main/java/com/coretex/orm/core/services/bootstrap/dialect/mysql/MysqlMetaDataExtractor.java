package com.coretex.orm.core.services.bootstrap.dialect.mysql;

import com.coretex.items.core.*;
import com.coretex.orm.core.services.bootstrap.AbstractMetaDataExtractor;
import com.coretex.orm.meta.AbstractGenericItem;
import com.coretex.relations.core.MetaAttributeOwnerRelation;
import com.coretex.relations.core.MetaEnumValueOwnerRelation;
import com.coretex.relations.core.MetaTypeInheritanceRelation;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class MysqlMetaDataExtractor extends AbstractMetaDataExtractor {

    private static final String QUOTE = "`";

    public MysqlMetaDataExtractor(DataSource dataSource) {
        super(dataSource);
    }

    public List<Map<String, Object>> selectTypeItems() {
        String typeAlias = "meta";
        String inhRefAlias = "mtir";
        String sql = "SELECT " +
                selectUUIDField(typeAlias, MetaTypeItem.UUID) + ", " +
                selectField(typeAlias, MetaTypeItem.TYPE_CODE) + ", " +
                selectField(typeAlias, MetaTypeItem.TABLE_NAME) + ", " +
                selectField(typeAlias, MetaTypeItem.ITEM_CLASS) + ", " +
                selectField(typeAlias, MetaTypeItem.TABLE_OWNER) + ", " +
                selectField(typeAlias, MetaTypeItem.DESCRIPTION) + ", " +
                selectField(typeAlias, MetaTypeItem.CREATE_DATE) + ", " +
                selectField(typeAlias, MetaTypeItem.UPDATE_DATE) + ", " +
                selectField(typeAlias, MetaTypeItem.ABSTRACT) + ", " +
                selectUUIDField(typeAlias, MetaRelationTypeItem.SOURCE_ATTRIBUTE) + ", " +
                selectUUIDField(typeAlias, MetaRelationTypeItem.TARGET_ATTRIBUTE) + ", " +
                selectUUIDField(typeAlias, MetaRelationTypeItem.SOURCE_TYPE) + ", " +
                selectUUIDField(typeAlias, MetaRelationTypeItem.TARGET_TYPE) + ", " +
                selectUUIDField(typeAlias, GenericItem.META_TYPE) + ", " +
                selectUUIDField(inhRefAlias, MetaTypeInheritanceRelation.TARGET, MetaTypeItem.PARENT) +
                " FROM " + fromTable(MetaTypeItem.ITEM_TYPE, typeAlias) +
                " LEFT JOIN " + fromTable(MetaTypeInheritanceRelation.ITEM_TYPE, inhRefAlias) + " ON " +
                onField(typeAlias, AbstractGenericItem.UUID) + " = " + onField(inhRefAlias, MetaTypeInheritanceRelation.SOURCE);
        return execute(sql);
    }

    public List<Map<String, Object>> selectAttributeItems() {
        String attrAlias = "attr";
        String ownerRelAlieas = "maor";
        String sql = "SELECT " +
                selectUUIDField(attrAlias, MetaAttributeTypeItem.UUID) + ", " +
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
                selectUUIDField(attrAlias, MetaAttributeTypeItem.ATTRIBUTE_TYPE) + ", " +
                selectField(attrAlias, MetaAttributeTypeItem.ATTRIBUTE_TYPE_CODE) + ", " +
                selectField(attrAlias, MetaAttributeTypeItem.CONTAINER_TYPE) + ", " +
                selectUUIDField(attrAlias, GenericItem.META_TYPE) + ", " +
                selectUUIDField(ownerRelAlieas, MetaAttributeOwnerRelation.TARGET, MetaAttributeTypeItem.OWNER)  +
                " FROM " + fromTable(MetaAttributeTypeItem.ITEM_TYPE, attrAlias) +
                " LEFT JOIN " + fromTable(MetaAttributeOwnerRelation.ITEM_TYPE, ownerRelAlieas) + " ON " +
                onField(attrAlias, AbstractGenericItem.UUID) + " = " + onField(ownerRelAlieas, MetaAttributeOwnerRelation.SOURCE);
        return execute(sql);
    }

    public List<Map<String, Object>> selectRegularItems() {
        String regularAlias = "reg";
        String sql = "SELECT " +
                selectUUIDField(regularAlias, RegularTypeItem.UUID) + ", " +
                selectField(regularAlias, RegularTypeItem.COLUMN_SIZE) + ", " +
                selectField(regularAlias, RegularTypeItem.DB_TYPE) + ", " +
                selectField(regularAlias, RegularTypeItem.PERSISTENCE_TYPE) + ", " +
                selectField(regularAlias, RegularTypeItem.REGULAR_CLASS) + ", " +
                selectField(regularAlias, RegularTypeItem.REGULAR_ITEM_CODE) + ", " +
                selectField(regularAlias, RegularTypeItem.CREATE_DATE) + ", " +
                selectField(regularAlias, RegularTypeItem.UPDATE_DATE) + ", " +
                selectUUIDField(regularAlias, GenericItem.META_TYPE) +
                " FROM " + fromTable(RegularTypeItem.ITEM_TYPE, regularAlias);
        return execute(sql);
    }

    public List<Map<String, Object>> selectEnumTypes() {
        String enumRef = "enu";
        String sql = "SELECT " +
                selectUUIDField(enumRef, AbstractGenericItem.UUID) + ", " +
                selectField(enumRef, MetaEnumTypeItem.ENUM_CODE) + ", " +
                selectField(enumRef, MetaEnumTypeItem.ENUM_CLASS) + ", " +
                selectField(enumRef, MetaEnumTypeItem.CREATE_DATE) + ", " +
                selectField(enumRef, MetaEnumTypeItem.UPDATE_DATE) + ", " +
                selectUUIDField(enumRef, GenericItem.META_TYPE) +
                " FROM " + fromTable(MetaEnumTypeItem.ITEM_TYPE, enumRef);
        return execute(sql);
    }

    public List<Map<String, Object>> selectEnumValues() {
        String enumValRef = "enuval";
        String ownerRef = "enuowner";
        String sql = "SELECT " +
                selectUUIDField(enumValRef, AbstractGenericItem.UUID) + ", " +
                selectField(enumValRef, MetaEnumValueTypeItem.CODE) + ", " +
                selectField(enumValRef, MetaEnumValueTypeItem.VALUE) + ", " +
                selectField(enumValRef, MetaEnumValueTypeItem.CREATE_DATE) + ", " +
                selectField(enumValRef, MetaEnumValueTypeItem.UPDATE_DATE) + ", " +
                selectUUIDField(enumValRef, GenericItem.META_TYPE) + ", " +
                selectUUIDField(ownerRef, MetaEnumValueOwnerRelation.TARGET, MetaEnumValueTypeItem.OWNER) +
                " FROM " + fromTable(MetaEnumValueTypeItem.ITEM_TYPE, enumValRef) +
                " LEFT JOIN " + fromTable(MetaEnumValueOwnerRelation.ITEM_TYPE, ownerRef) + " ON " +
                onField(enumValRef, AbstractGenericItem.UUID) + " = " + onField(ownerRef, MetaEnumValueOwnerRelation.SOURCE);
        return execute(sql);
    }


    private String fromTable(String tableName, String alias) {
        return QUOTE + wrapTable(tableName.toLowerCase()) + QUOTE + " as " + alias;
    }

    private String wrapTable(String table) {
        return "t_" + table;//todo: make prefix it configurable
    }

    private String wrapUUID(String column){
        return "BIN_TO_UUID("+column+")";
    }

    private String onField(String tableAlias, String attributeName) {
        return tableAlias + '.' + getColumn(attributeName);
    }
    private String selectUUIDField(String tableAlias, String attributeName) {
        return selectUUIDField(tableAlias, attributeName, attributeName);
    }

    private String selectUUIDField(String tableAlias, String attributeName, String alias) {
        return wrapUUID(tableAlias + '.' + getColumn(attributeName)) + " as " + QUOTE + alias + QUOTE;
    }

    private String selectField(String tableAlias, String attributeName) {
        return selectField(tableAlias, attributeName, attributeName);
    }

    private String selectField(String tableAlias, String attributeName, String alias) {
        return tableAlias + '.' + getColumn(attributeName) + " as " + QUOTE + alias + QUOTE;
    }

    private String getColumn(String attributeName) {
        return QUOTE + (attributeName.equals(AbstractGenericItem.UUID) ? attributeName : wrapColumn(attributeName)).toLowerCase() + QUOTE;
    }

    private String wrapColumn(String attributeName) {
        return "c_" + attributeName;
    }
}
