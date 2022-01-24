package com.coretex.build.services.impl.mysql.query

import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.inserts.Insert
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RegularClassItem
import com.coretex.build.services.StatementQueryFactory

import static com.coretex.common.utils.VmUtils.isUUIDType

class MySqlStatementQueryFactory implements StatementQueryFactory {

    @Override
    String insertInto(Insert insert) {
        return """
        INSERT INTO ${-> insert.essentialItem.tableName} (uuid, ${insert.columns.collect({ it.columnName }).join(",")} )\n
        VALUES (UUID_TO_BIN('${insert.uuid}'), ${insert.columns.collect({ isUUIDType(it.rawValue) ? "UUID_TO_BIN(${it.value})" : it.value }).join(",")});
        """
    }

    @Override
    String update(String tableName, UUID uuid, List<Column> columns) {
        return """
        UPDATE ${tableName} 
        SET ${columns.collect({ "${it.columnName} = ${isUUIDType(it.rawValue) ? "UUID_TO_BIN(${it.value})" : it.value}" }).join(",")} 
        WHERE uuid = UUID_TO_BIN('${uuid}');
        """
    }

    @Override
    String deleteFrom(String tableName, Map<String, String> params) {
        return """
        DELETE FROM ${tableName} as t
               WHERE ${params.entrySet().collect({"t.${it.key()} = ${isUUIDType(it.value) ? "UUID_TO_BIN(${it.value})" : it.value}"}).join(" AND ")};
        """
    }

    @Override
    String selectAllFromMetaEnumType() {
        return """SELECT BIN_TO_UUID(mt.uuid) uuid, * FROM public.t_metaenumtype as mt"""
    }

    @Override
    String selectAllFromRegularType() {
        return """SELECT BIN_TO_UUID(mt.uuid) uuid, * FROM public.t_regulartype as mt"""
    }

    @Override
    String selectClassItemsByMetaType(ClassItem metaTypeItem){
        return """SELECT mt.*, BIN_TO_UUID(mt.uuid) uuid, BIN_TO_UUID(mtir.c_target) ${AbstractClassItemDiffDataHolder.PARENT_UUID} FROM public.t_metatype as mt 
                                                                        LEFT JOIN public.t_metatypeinheritancerelation as mtir ON (mtir.c_source = mt.uuid)
                                                                        WHERE mt.c_metatype = UUID_TO_BIN('${metaTypeItem.uuid}')"""
    }

    @Override
    String selectClassItemByCodeAndType(AbstractItem item){
        return """SELECT mt.*, BIN_TO_UUID(mt.uuid) uuid, BIN_TO_UUID(mtir.c_target) ${AbstractClassItemDiffDataHolder.PARENT_UUID} FROM public.t_metatype as mt 
                                                                        LEFT JOIN public.t_metatypeinheritancerelation as mtir ON (mtir.c_source = mt.uuid)
                                                                        WHERE mt.c_typecode = '${item.code}'
                                                                        AND mt.c_itemclass = '${item.fullTypeName}'"""
    }

    @Override
    String selectRegularItem(RegularClassItem item) {
        return """SELECT BIN_TO_UUID(mt.uuid) uuid, * FROM public.t_regulartype as mt 
                    WHERE mt.c_regularclass ='${item.regularClass.name}'
                    AND mt.c_regularitemcode = '${item.code}'"""
    }

    @Override
    String selectEnumType(AbstractItem item){
        return """SELECT BIN_TO_UUID(mt.uuid) uuid, * FROM public.t_metaenumtype as mt 
                    WHERE mt.c_enumcode ='${item.code}'
                    AND mt.c_enumclass = '${item.fullTypeName}'"""
    }

    @Override
    String selectEnumValueTypeByUUID(String uuid) {
        return """SELECT BIN_TO_UUID(mevt.uuid) uuid, BIN_TO_UUID(re.uuid) relUuid
                    FROM public.t_metaenumvaluetype as mevt 
                    JOIN t_metaenumvalueownerrelation as re ON mevt.uuid = re.c_source
                    WHERE re.c_target = UUID_TO_BIN('${uuid}')"""
    }

    @Override
    String selectEnumValueTypeByUUIDAndCodeAndValue(String uuid, String code, String value) {
        return """SELECT BIN_TO_UUID(mevt.uuid) uuid, BIN_TO_UUID(re.uuid) relUuid
                    FROM public.t_metaenumvaluetype as mevt 
                    JOIN t_metaenumvalueownerrelation as re ON mevt.uuid = re.c_source
                    WHERE re.c_target = UUID_TO_BIN('${uuid}')
                    AND mevt.c_code ='${code}' 
                    AND mevt.c_value = '${value}'"""
    }

    @Override
    String selectLocTable(String tableName){
        return """SELECT table_name as name FROM information_schema.tables 
                    WHERE table_schema = 'public'
                    AND table_name = '${tableName}_loc'"""
    }

    @Override
    String selectAttributesByOwnerUUID(String uuid){
        return """SELECT BIN_TO_UUID(mat.uuid) uuid, mat.* 
                    FROM public.t_metaattributetype as mat 
                    JOIN public.t_metaattributeownerrelation as maor ON maor.c_source = mat.uuid 
                    WHERE maor.c_target in (UUID_TO_BIN('${uuid}'))"""
    }

    @Override
    String selectAttributesByOwnerUUIDAndAttributeNameAndColumnName(String ownerUuid, String attributeName, String columnName){
        return """SELECT BIN_TO_UUID(mat.uuid), mat.*, BIN_TO_UUID(maor.c_target) owner 
                    FROM public.t_metaattributetype as mat 
                    JOIN public.t_metaattributeownerrelation as maor ON maor.c_source = mat.uuid 
                    WHERE maor.c_target in (UUID_TO_BIN('${ownerUuid}')) 
                    AND (lower(mat.c_attributename)= lower('${attributeName}') 
                    OR lower(mat.c_columnname)=lower('${columnName}'))"""
    }

    @Override
    String selectIndexesByTableAndColumn(String tableName, String columnName){
        return """SELECT INDEX_NAME as indexName, 
                         TABLE_NAME, 
                         COLUMN_NAME 
                    FROM INFORMATION_SCHEMA.STATISTICS
                    WHERE TABLE_NAME = '${tableName}'
                    AND COLUMN_NAME = '${columnName}'"""
    }
}
