package com.coretex.build.services.impl.postgres.query

import com.coretex.build.data.db.Table
import com.coretex.build.data.db.TableField
import com.coretex.build.services.TableQueryFactory

class PostgresqlTableQueryFactory implements TableQueryFactory{

    @Override
    String dropTable(String name){
        return """DROP TABLE IF EXISTS ${name} CASCADE;"""
    }

    @Override
    String createTable(Table table) {
        return """CREATE TABLE ${table.name} (
                  uuid UUID PRIMARY KEY,
                  ${table.fields.collect({ "${it.name} ${it.sqlType}" }).join(",\n")}
                  );"""
    }

    @Override
    String crateIndex(String name, String tableName, Set<TableField> fields) {
        return """CREATE INDEX CONCURRENTLY IF NOT EXISTS ${name} ON ${tableName} (${fields.stream().map({it.name}).toArray().join(",")});"""
    }

    @Override
    String dropIndex(String indexName) {
        return """DROP INDEX CONCURRENTLY IF EXISTS ${indexName};"""
    }

    @Override
    String createLocTable(String name){
        return """CREATE TABLE ${name}_loc (\n 
                  owner UUID,\n 
                  attribute UUID,\n 
                  localeISO VARCHAR(48),\n 
                  value TEXT,\n 
                  PRIMARY KEY(owner, attribute, localeISO)\n 
                  );"""
    }

    @Override
    String alterTableAdd(String tableName, String colName, String type){
        return """ALTER TABLE ${tableName} ADD ${colName} ${type};"""
    }

    @Override
    String alterTableDrop(String tableName, String colName){
        return """ALTER TABLE ${tableName} DROP COLUMN ${colName};"""
    }

}
