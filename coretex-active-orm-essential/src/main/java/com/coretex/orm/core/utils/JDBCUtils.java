package com.coretex.orm.core.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class JDBCUtils {
	private static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

	public static Calendar utc() {
		return (Calendar) UTC.clone();
	}

	public static List<String> columns(final ResultSet r) throws SQLException {
		ResultSetMetaData resultSetMetaData = r.getMetaData();
		int count = resultSetMetaData.getColumnCount();

		ImmutableList.Builder<String> columns = ImmutableList.builder();

		for (int i = 1; i <= count; i++) {
			columns.add(resultSetMetaData.getColumnName(i));
		}
		return columns.build();
	}

	public static List<Map<String, Object>> result(final ResultSet r) throws SQLException {
		ImmutableList.Builder<Map<String, Object>> list = ImmutableList.builder();
		ResultSetMetaData meta = r.getMetaData();
		while (r.next()) {
			Map<String, Object> row = row(meta, r);
			if (row.size() > 0) {
				list.add(row);
			}
		}
		return list.build();
	}


	private static Object pgArrayToArray(String columnName, Object value, int jdbcType) throws SQLException {
		if (value == null || jdbcType != Types.ARRAY) {
			return value;
		}


		if (value instanceof List) {
			return value;
		} else {
			throw new UnsupportedOperationException("PgArray not supported yet");
//			// BIGINT is the base type for Long pgArray that we store in crate db.
//			PgArray pgArray = (PgArray) value;
//			if (pgArray.getBaseType() != BIGINT) {
//				return StaticHelper.pgArrayToStringList(value);
//			} else {
//				return StaticHelper.pgArrayToLongList(value);
//			}
		}
	}


	private static Map<String, Object> row(ResultSetMetaData metaData, ResultSet r) throws SQLException {
		Map<String, Object> map = Maps.newHashMap();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			String columnName = metaData.getColumnName(i);
			String dataType = metaData.getColumnTypeName(i);
			int jdbcType = metaData.getColumnType(i);

			if (dataType.equalsIgnoreCase("timestamptz")) {
				Timestamp ts = r.getTimestamp(i, utc());
				map.put(columnName, ts.toLocalDateTime());
			} else {
				map.put(columnName, pgArrayToArray(columnName, r.getObject(i), jdbcType));
			}
		}
		return Collections.unmodifiableMap(map);
	}

	public static List<Map<String, Object>> result(final ResultSet r, Collection<String> columns) throws SQLException {
		List<Map<String, Object>> result = result(r);

		List<Map<String, Object>> list = new ArrayList<>();
		for (Map<String, Object> m : result) {
			Map<String, Object> collect = new HashMap<>();
			for (Map.Entry<String, Object> e : m.entrySet()) {
				if (columns.contains(e.getKey())) {
					if (collect.put(e.getKey(), e.getValue()) != null) {
						throw new IllegalStateException("Duplicate key");
					}
				}
			}
			list.add(collect);
		}
		return list;
	}


}
