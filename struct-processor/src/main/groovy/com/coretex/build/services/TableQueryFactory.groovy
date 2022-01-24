package com.coretex.build.services

import com.coretex.build.data.db.Table
import com.coretex.build.data.db.TableField

interface TableQueryFactory {
    String dropTable(String name)
    String createTable(Table table)
    String crateIndex(String name, String tableName, Set<TableField> fields)
    String dropIndex(String indexName)
    String createLocTable(String name)
    String alterTableAdd(String tableName, String colName, String type)
    String alterTableDrop(String tableName, String colName)
}
