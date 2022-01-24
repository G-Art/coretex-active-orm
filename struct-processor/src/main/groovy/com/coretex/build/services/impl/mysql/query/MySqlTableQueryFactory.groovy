package com.coretex.build.services.impl.mysql.query

import com.coretex.build.data.db.Table
import com.coretex.build.data.db.TableField
import com.coretex.build.services.TableQueryFactory
import com.mysql.cj.MysqlType

class MySqlTableQueryFactory implements TableQueryFactory {

    @Override
    String dropTable(String name) {
        return """DROP TABLE IF EXISTS ${name} CASCADE;"""
    }

    @Override
    String createTable(Table table) {
        return """CREATE TABLE ${table.name} (\n uuid BINARY(16) PRIMARY KEY,\n
${table.fields.collect({ "${it.name} ${it.sqlType}" }).join(",\n")}
);"""
    }

    @Override
    String crateIndex(String name, String tableName, Set<TableField> fields) {
        return """CREATE INDEX ${name} ON ${tableName} (${fields.stream().map({ (it.sqlType == MysqlType.LONGTEXT.getName() ? "${it.name}(767)" : it.name) }).toArray().join(",")});"""
    }

    @Override
    String dropIndex(String indexName) {
        return """DROP INDEX ${indexName};"""
    }

    @Override
    String createLocTable(String name) {
        return """CREATE TABLE ${name}_loc (\n
owner BINARY(16),\n
attribute BINARY(16),\n
localeISO VARCHAR(48),\n
value LONGTEXT,\n
PRIMARY KEY(owner, attribute, localeISO)\n
) ENGINE=INNODB;"""
    }

    @Override
    String alterTableAdd(String tableName, String colName, String type) {
        return """ALTER TABLE ${tableName} ADD ${colName} ${type};"""
    }

    @Override
    String alterTableDrop(String tableName, String colName) {
        return """ALTER TABLE ${tableName} DROP COLUMN ${colName};"""
    }
}
