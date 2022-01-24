package com.coretex.build.services.impl.postgres.query

import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.inserts.Insert
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RegularClassItem
import com.coretex.build.services.StatementQueryFactory

class PostgresqlStatementQueryFactory implements StatementQueryFactory {

    @Override
    String insertInto(Insert insert) {
        return """
        INSERT INTO ${-> insert.essentialItem.tableName} (uuid, ${insert.columns.collect({ it.columnName }).join(",")} )\n
        VALUES ('${insert.uuid}', ${insert.columns.collect({ it.value }).join(",")});
        """
    }
    @Override
    String update(String tableName, UUID uuid, List<Column> columns) {
        return """
        UPDATE ${tableName} SET ${columns.collect({ "${it.columnName} = ${it.value}" }).join(",")} WHERE uuid = '${uuid}';
        """
    }

    @Override
    String deleteFrom(String tableName, Map<String, String> params) {
        return """
        DELETE FROM ${tableName} as t
                WHERE ${params.entrySet().collect({ "t.${it.key()} = ${it.value()}" }).join(" AND ")};
        """
    }

    @Override
    String selectAllFromMetaEnumType() {
        return """SELECT * FROM public.t_metaenumtype as mt"""
    }

    @Override
    String selectAllFromRegularType() {
        return """SELECT * FROM public.t_regulartype as mt"""
    }

    @Override
    String selectClassItemsByMetaType(ClassItem metaTypeItem) {
        return """SELECT mt.*, mtir.c_target as ${AbstractClassItemDiffDataHolder.PARENT_UUID} FROM public.t_metatype as mt 
                    LEFT JOIN public.t_metatypeinheritancerelation as mtir ON (mtir.c_source = mt.uuid)
                    WHERE mt.c_metatype = '${metaTypeItem.uuid}'"""
    }

    @Override
    String selectClassItemByCodeAndType(AbstractItem item) {
        return """SELECT mt.*, mtir.c_target as ${AbstractClassItemDiffDataHolder.PARENT_UUID} FROM public.t_metatype as mt 
                    LEFT JOIN public.t_metatypeinheritancerelation as mtir ON (mtir.c_source = mt.uuid)
                    WHERE mt.c_typecode = '${item.code}'
                    AND mt.c_itemclass = '${item.fullTypeName}'"""
    }

    @Override
    String selectRegularItem(RegularClassItem item) {
        return """SELECT * FROM public.t_regulartype as mt 
                    WHERE mt.c_regularclass ='${item.regularClass.name}'
                    AND mt.c_regularitemcode = '${item.code}'"""
    }

    @Override
    String selectEnumType(AbstractItem item) {
        return """SELECT * FROM public.t_metaenumtype as mt 
                    WHERE mt.c_enumcode ='${item.code}'
                    AND mt.c_enumclass = '${item.fullTypeName}'"""
    }

    @Override
    String selectEnumValueTypeByUUID(String uuid) {
        return """SELECT mevt.uuid as uuid, re.uuid as relUuid
                    FROM public.t_metaenumvaluetype as mevt 
                    JOIN t_metaenumvalueownerrelation as re ON mevt.uuid = re.c_source
                    WHERE re.c_target = '${uuid}'"""
    }

    @Override
    String selectEnumValueTypeByUUIDAndCodeAndValue(String uuid, String code, String value) {
        return """SELECT mevt.uuid as uuid, re.uuid as relUuid
                    FROM public.t_metaenumvaluetype as mevt 
                    JOIN t_metaenumvalueownerrelation as re ON mevt.uuid = re.c_source
                    WHERE re.c_target = '${uuid}'
                    AND mevt.c_code ='${code}' 
                    AND mevt.c_value = '${value}'"""
    }

    @Override
    String selectLocTable(String tableName) {
        return """SELECT table_name as name FROM information_schema.tables 
                                      WHERE table_schema = 'public'
                                      AND table_name = '${tableName}_loc'"""
    }

    @Override
    String selectAttributesByOwnerUUID(String uuid) {
        return """SELECT mat.* 
                    FROM public.t_metaattributetype as mat 
                    JOIN public.t_metaattributeownerrelation as maor ON maor.c_source = mat.uuid 
                    WHERE maor.c_target in ('${uuid}')"""
    }

    @Override
    String selectAttributesByOwnerUUIDAndAttributeNameAndColumnName(String ownerUuid, String attributeName, String columnName) {
        return """SELECT mat.*, maor.c_target as owner 
                    FROM public.t_metaattributetype as mat 
                    JOIN public.t_metaattributeownerrelation as maor ON maor.c_source = mat.uuid 
                    WHERE maor.c_target in ('${ownerUuid}') 
                    AND (lower(mat.c_attributename)= lower('${attributeName}') 
                    OR lower(mat.c_columnname)=lower('${columnName}'))"""
    }

    @Override
    String selectIndexesByTableAndColumn(String tableName, String columnName) {
        return """SELECT i.relname as indexName
                    FROM    pg_class t,
                            pg_class i,
                            pg_index ix,
                            pg_attribute a,
                            pg_tables ta
                    WHERE t.oid = ix.indrelid
                    AND i.oid = ix.indexrelid
                    AND ta.tablename= t.relname
                    AND a.attrelid = t.oid
                    AND a.attnum = ANY (ix.indkey)
                    AND t.relkind = 'r'
                    AND ta.schemaname ='public'
                    AND ta.tablename = '${tableName}'
                    AND a.attname = '${columnName}'
                    ORDER BY
                    t.relname,
                    i.relname; """
    }

}
