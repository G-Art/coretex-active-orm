package com.coretex.orm.core.activeorm.query.rex;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexBiVisitor;
import org.apache.calcite.rex.RexDynamicParam;
import org.apache.calcite.rex.RexVisitor;
import org.apache.calcite.sql.SqlKind;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public class RexDynamicNamedParam extends RexDynamicParam {
	//~ Instance fields --------------------------------------------------------

	private final int index;
	private final String name;
	private final String fullName;

	//~ Constructors -----------------------------------------------------------

	/**
	 * Creates a dynamic named parameter.
	 *
	 * @param name inferred name of named parameter
	 * @param type  inferred type of parameter
	 * @param index 0-based index of dynamic named parameter in statement
	 */
	public RexDynamicNamedParam(
			String name,
			RelDataType type,
			int index) {
		super(type, index);
		this.name = name;
		this.fullName = ":" + name;
		this.index = index;
		this.digest = Objects.requireNonNull(fullName, "name");
	}

	//~ Methods ----------------------------------------------------------------

	@Override public SqlKind getKind() {
		return SqlKind.DYNAMIC_PARAM;
	}

	public int getIndex() {
		return index;
	}

	@Override public <R> R accept(RexVisitor<R> visitor) {
		return visitor.visitDynamicParam(this);
	}

	@Override public <R, P> R accept(RexBiVisitor<R, P> visitor, P arg) {
		return visitor.visitDynamicParam(this, arg);
	}

	@Override
	public String getName() {
		return RexDynamicNamedParam.this.name;
	}

	public String getFullName() {
		return fullName;
	}

	@Override public boolean equals(@Nullable Object obj) {
		return this == obj
				|| obj instanceof RexDynamicNamedParam
				&& type.equals(((RexDynamicNamedParam) obj).type)
				&& Objects.equals(RexDynamicNamedParam.this.name, ((RexDynamicNamedParam) obj).name);
	}

	@Override public int hashCode() {
		return Objects.hash(type, index);
	}
}
