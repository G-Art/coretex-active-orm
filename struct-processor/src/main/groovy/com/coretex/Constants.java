package com.coretex;

import java.io.File;

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
public final  class Constants {

    private Constants() {
    }
    public static final String PROJECT = "project";

    public static final String DEFAULT_GEN_PACKAGE = "com.coretex";

    public static final String DEFAULT_DB_DIALECT = "postgresql";

    public static final String TYPE_ENUM = "enum";
    public static final String TYPE_ITEM = "item";
    public static final String TYPE_RELATION = "relation";
    public static final String SQL_SCHEMA = "sql_schema";
    public static final String SQL_SCHEMA_FILE_NAME = "init_sql_schema.sql";

    public static final String PROPERTY_GEN_JAVA_SRC = "javaSrc";
    public static final String PROPERTY_PACKAGE = "generatePackageDir";
    public static final String PROPERTY_MODULE_FOR_GEN_MODEL = "moduleForModelGen";

    public static final String PATH_SEPARATOR = File.separator;

    public static final String FIELD_ATTRIBUTE_NAME = "field";
    public static final String ATTRIBUTE_PARAM_NAME = "param";
    public static final String COLLECTION_TYPE_LIST = "list";
    public static final String COLLECTION_TYPE_SET = "set";

    public static final String COLUMN_PREFIX = "c_";
    public static final String TABLE_PREFIX = "t_";
    public static final String INDEX_PREFIX = "coretex_i_";

    public static final String PATH_CLASSES = "classes";

    public static final class Config {
        private Config() {
        }

        static final class DB {
            private DB() {
            }

            public static final String DB_DIALECT_CONFIG = "db.dialect";
        }

        static final class Build {
            private Build() {
            }

            public static final String STRUCT_ANALISE = "build.struct.analise";
        }

    }
}
