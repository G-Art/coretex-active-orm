package com.coretex.build.services.impl.postgres

import com.coretex.build.services.TypeAppropriateService
import com.google.common.collect.ImmutableMap

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import org.postgresql.core.Oid

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class PostgresqlTypeAppropriateService extends TypeAppropriateService {

    private static final String PSQL_UUID       = Oid.toString(Oid.UUID)
    private static final String PSQL_TEXT       = Oid.toString(Oid.TEXT)
    private static final String PSQL_SHORT      = Oid.toString(Oid.INT2)
    private static final String PSQL_INTEGER    = Oid.toString(Oid.INT4)
    private static final String PSQL_INT8       = Oid.toString(Oid.INT8)
    private static final String PSQL_TIMESTAMPTZ= Oid.toString(Oid.TIMESTAMPTZ)
    private static final String PSQL_TIMESTAMP  = Oid.toString(Oid.TIMESTAMP)
    private static final String PSQL_DATE       = Oid.toString(Oid.DATE)
    private static final String PSQL_TIME       = Oid.toString(Oid.TIME)
    private static final String PSQL_FLOAT8     = Oid.toString(Oid.FLOAT8)
    private static final String PSQL_FLOAT4     = Oid.toString(Oid.FLOAT4)
    private static final String PSQL_CHARACTER  = Oid.toString(Oid.CHAR)
    private static final String PSQL_BOOLEAN    = Oid.toString(Oid.BOOL)
    public static final String PSQL_NULL        = 'NULL'

    private ImmutableMap<Class, String> cTypes

    private ImmutableMap<Class, String> persistenceMappingCtx

    @Override
    ImmutableMap<Class<?>, String> connectedTypes() {
        return cTypes
    }

    @Override
    ImmutableMap<Class<?>, String> persistenceMapping() {
        return persistenceMappingCtx
    }

    @Override
    protected void init() {
        this.cTypes = ImmutableMap.builder()
                .put(UUID, PSQL_UUID)
                .put(String, PSQL_TEXT)
                .put(Integer, PSQL_INTEGER)
                .put(Long, PSQL_INT8)
                .put(Date, PSQL_TIMESTAMP)
                .put(Double, PSQL_FLOAT8)
                .put(Short, PSQL_SHORT)
                .put(Byte, PSQL_SHORT)
                .put(BigDecimal, PSQL_FLOAT8)
                .put(Float, PSQL_FLOAT4)
                .put(Character, PSQL_CHARACTER)
                .put(Boolean, PSQL_BOOLEAN)
                .put(Class, PSQL_TEXT)
                .put(LocalDate, PSQL_DATE)
                .put(LocalDateTime, PSQL_TIMESTAMP)
                .put(LocalTime, PSQL_TIME)
                .build()

        this.persistenceMappingCtx = ImmutableMap.builder()
                .put(UUID, 'pg-uuid')
                .put(String, String.simpleName.toLowerCase())
                .put(Integer, int.simpleName)
                .put(Long, long.simpleName)
                .put(Date, Date.simpleName.toLowerCase())
                .put(Double, double.simpleName)
                .put(BigDecimal, 'big_decimal')
                .put(Short, short.simpleName)
                .put(Byte, byte.simpleName)
                .put(Float, float.simpleName)
                .put(Character, Character.simpleName.toLowerCase())
                .put(Boolean, boolean.simpleName)
                .put(Class, Class.simpleName.toLowerCase())
                .put(LocalDate, LocalDate.simpleName)
                .put(LocalDateTime, LocalDateTime.simpleName)
                .put(LocalTime, LocalTime.simpleName)
                .build()
    }

    @Override
    String getNullValue() {
        return PSQL_NULL
    }
}
