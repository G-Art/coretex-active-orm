package com.coretex.build.services.impl.mysql

import com.coretex.build.services.TypeAppropriateService
import com.google.common.collect.ImmutableMap
import org.postgresql.core.Oid
import com.mysql.cj.MysqlType

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MySqlTypeAppropriateService extends TypeAppropriateService {

	private static final String MYSQL_UUID       = "${MysqlType.BINARY.getName()}(16)"
	private static final String MYSQL_TEXT       = MysqlType.LONGTEXT.getName()
	private static final String MYSQL_SHORT      = MysqlType.SMALLINT.getName()
	private static final String MYSQL_INTEGER    = MysqlType.INT.getName()
	private static final String MYSQL_INT8       = MysqlType.BIGINT.getName()
	private static final String MYSQL_TIMESTAMPTZ= MysqlType.DATETIME.getName()
	private static final String MYSQL_TIMESTAMP  = "${MysqlType.DATETIME.getName()}(6)"
	private static final String MYSQL_DATE       = MysqlType.DATE.getName()
	private static final String MYSQL_TIME       = MysqlType.TIME.getName()
	private static final String MYSQL_FLOAT8     = MysqlType.DOUBLE.getName()
	private static final String MYSQL_FLOAT4     = MysqlType.FLOAT.getName()
	private static final String MYSQL_CHARACTER  = MysqlType.CHAR.getName()
	private static final String MYSQL_BOOLEAN    = MysqlType.BOOLEAN.getName()
	public static final String MYSQL_NULL        = 'NULL'


	private ImmutableMap<Class, String> cTypes

	private ImmutableMap<Class, String> persistenceMappingCtx

	@Override
	ImmutableMap<Class, String> connectedTypes() {
		return cTypes
	}

	@Override
	ImmutableMap<String, String> persistenceMapping() {
		return persistenceMappingCtx
	}

	@Override
	String getNullValue() {
		return MYSQL_NULL
	}

	@Override
	protected void init() {
		this.cTypes = ImmutableMap.builder()
				.put(UUID, MYSQL_UUID)
				.put(String, MYSQL_TEXT)
				.put(Integer, MYSQL_INTEGER)
				.put(Long, MYSQL_INT8)
				.put(Date, MYSQL_TIMESTAMP)
				.put(Double, MYSQL_FLOAT8)
				.put(Short, MYSQL_SHORT)
				.put(Byte, MYSQL_SHORT)
				.put(BigDecimal, MYSQL_FLOAT8)
				.put(Float, MYSQL_FLOAT8)
				.put(Character, MYSQL_CHARACTER)
				.put(Boolean, MYSQL_BOOLEAN)
				.put(Class, MYSQL_TEXT)
				.put(LocalDate, MYSQL_DATE)
				.put(LocalDateTime, MYSQL_TIMESTAMP)
				.put(LocalTime, MYSQL_TIME)
				.build()

		this.persistenceMappingCtx = ImmutableMap.builder()
				.put(UUID, 'uuid')
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
}
