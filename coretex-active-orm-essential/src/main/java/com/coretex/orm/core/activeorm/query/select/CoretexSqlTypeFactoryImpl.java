package com.coretex.orm.core.activeorm.query.select;

import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import org.apache.calcite.rel.type.*;
import org.apache.calcite.sql.SqlCollation;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.sql.type.SqlTypeUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class CoretexSqlTypeFactoryImpl extends SqlTypeFactoryImpl {

	private static final LoadingCache<Key, RelDataType> KEY2TYPE_CACHE =
			CacheBuilder.newBuilder()
					.softValues()
					.build(CacheLoader.from(CoretexSqlTypeFactoryImpl::keyToType));


	public CoretexSqlTypeFactoryImpl() {
		this(RelDataTypeSystem.DEFAULT);
	}

	public CoretexSqlTypeFactoryImpl(RelDataTypeSystem typeSystem) {
		super(typeSystem);
	}

	private static RelDataType keyToType(Key key) {
		final ImmutableList.Builder<RelDataTypeField> list =
				ImmutableList.builder();
		for (int i = 0; i < key.names.size(); i++) {
			String name = key.names.get(i);
			RelDataType type = key.types.get(i);
			var field = type instanceof CoretexSqlType
					? type.getField(name, false, false)
					: null;

			field = type instanceof CoretexBasicSqlType
					? ((CoretexBasicSqlType) type).getFieldFor()
					: field;

			list.add(
					Objects.nonNull(field)
							? field.getName().equals(name) ? field : new RelDataTypeFieldImpl(name, i, type)
							: new RelDataTypeFieldImpl(name, i, type)
			);
		}
		return new RelRecordType(key.kind, list.build(), key.nullable);
	}

	@Override public RelDataType createSqlType(SqlTypeName typeName) {
		if (typeName.allowsPrec()) {
			return createSqlType(typeName, typeSystem.getDefaultPrecision(typeName));
		}
		assertBasic(typeName);
		RelDataType newType = new CoretexBasicSqlType(typeSystem, typeName);
		return canonize(newType);
	}

	@Override public RelDataType createSqlType(
			SqlTypeName typeName,
			int precision) {
		final int maxPrecision = typeSystem.getMaxPrecision(typeName);
		if (maxPrecision >= 0 && precision > maxPrecision) {
			precision = maxPrecision;
		}
		if (typeName.allowsScale()) {
			return createSqlType(typeName, precision, typeName.getDefaultScale());
		}
		assertBasic(typeName);
		assert (precision >= 0)
				|| (precision == RelDataType.PRECISION_NOT_SPECIFIED);
		// Does not check precision when typeName is SqlTypeName#NULL.
		RelDataType newType = precision == RelDataType.PRECISION_NOT_SPECIFIED
				? new CoretexBasicSqlType(typeSystem, typeName)
				: new CoretexBasicSqlType(typeSystem, typeName, precision);
		newType = SqlTypeUtil.addCharsetAndCollation(newType, this);
		return canonize(newType);
	}

	@Override public RelDataType createSqlType(
			SqlTypeName typeName,
			int precision,
			int scale) {
		assertBasic(typeName);
		assert (precision >= 0)
				|| (precision == RelDataType.PRECISION_NOT_SPECIFIED);
		final int maxPrecision = typeSystem.getMaxPrecision(typeName);
		if (maxPrecision >= 0 && precision > maxPrecision) {
			precision = maxPrecision;
		}
		RelDataType newType =
				new CoretexBasicSqlType(typeSystem, typeName, precision, scale);
		newType = SqlTypeUtil.addCharsetAndCollation(newType, this);
		return canonize(newType);
	}

	private static void assertBasic(SqlTypeName typeName) {
		assert typeName != null;
		assert typeName != SqlTypeName.MULTISET
				: "use createMultisetType() instead";
		assert typeName != SqlTypeName.ARRAY
				: "use createArrayType() instead";
		assert typeName != SqlTypeName.MAP
				: "use createMapType() instead";
		assert typeName != SqlTypeName.ROW
				: "use createStructType() instead";
		assert !SqlTypeName.INTERVAL_TYPES.contains(typeName)
				: "use createSqlIntervalType() instead";
	}

	@Override
	public RelDataType createTypeWithNullability(
			final RelDataType type,
			final boolean nullable) {
		final RelDataType newType;

		if (type instanceof CoretexSqlType) {
			newType = ((CoretexSqlType) type).createWithNullability(nullable);
		} else if (type instanceof CoretexBasicSqlType) {
			newType = ((CoretexBasicSqlType) type).createWithNullability(nullable);
		}
		else {
			return super.createTypeWithNullability(type, nullable);
		}
		return canonize(newType);
	}

	@Override
	public RelDataType canonize(RelDataType type) {
		return type;
	}

	public RelDataType canonize(final StructKind kind,
	                            final List<String> names,
	                            final List<RelDataType> types,
	                            final boolean nullable) {
		final RelDataType type = KEY2TYPE_CACHE.getIfPresent(
				new Key(kind, names, types, nullable));
		if (type != null) {
			return type;
		}
		final ImmutableList<String> names2 = ImmutableList.copyOf(names);
		final ImmutableList<RelDataType> types2 = ImmutableList.copyOf(types);
		return KEY2TYPE_CACHE.getUnchecked(new Key(kind, names2, types2, nullable));
	}

	@Override public RelDataType createTypeWithCharsetAndCollation(
			RelDataType type,
			Charset charset,
			SqlCollation collation) {
		assert SqlTypeUtil.inCharFamily(type) : type;
		requireNonNull(charset, "charset");
		requireNonNull(collation, "collation");
		if (type instanceof CoretexBasicSqlType) {
			CoretexBasicSqlType sqlType = (CoretexBasicSqlType) type;
			return sqlType.createWithCharsetAndCollation(charset, collation);
		} else {
			return super.createTypeWithCharsetAndCollation(type, charset, collation);
		}
	}

	/**
	 * Key to the data type cache.
	 */
	private static class Key {
		private final StructKind kind;
		private final List<String> names;
		private final List<RelDataType> types;
		private final boolean nullable;

		Key(StructKind kind, List<String> names, List<RelDataType> types, boolean nullable) {
			this.kind = kind;
			this.names = names;
			this.types = types;
			this.nullable = nullable;
		}

		@Override
		public int hashCode() {
			return Objects.hash(kind, names, types, nullable);
		}

		@Override
		public boolean equals(@Nullable Object obj) {
			return obj == this
					|| obj instanceof Key
					&& kind == ((Key) obj).kind
					&& names.equals(((Key) obj).names)
					&& types.equals(((Key) obj).types)
					&& nullable == ((Key) obj).nullable;
		}
	}
}
