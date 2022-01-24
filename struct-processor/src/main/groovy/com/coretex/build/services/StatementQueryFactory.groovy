package com.coretex.build.services

import com.coretex.build.data.db.inserts.Insert
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RegularClassItem

interface StatementQueryFactory {
    String insertInto(Insert insert)
    String update(String tableName, UUID uuid, List<Column> columns)
    String deleteFrom(String tableName, Map<String, String> params)

    String selectAllFromMetaEnumType()
    String selectAllFromRegularType()
    String selectClassItemsByMetaType(ClassItem metaTypeItem)
    String selectClassItemByCodeAndType(AbstractItem item)
    String selectRegularItem(RegularClassItem item)
    String selectEnumType(AbstractItem item)
    String selectEnumValueTypeByUUID(String uuid)
    String selectEnumValueTypeByUUIDAndCodeAndValue(String uuid, String code, String value)
    String selectLocTable(String tableName)
    String selectAttributesByOwnerUUID(String uuid)
    String selectAttributesByOwnerUUIDAndAttributeNameAndColumnName(String ownerUuid, String attributeName, String columnName)
    String selectIndexesByTableAndColumn(String tableName, String columnName)
}
