package com.coretex.orm.core.activeorm.query.select;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rel.type.RelRecordType;
import org.apache.calcite.rel.type.StructKind;

import java.util.List;

public class CoretexSqlType extends RelRecordType {

	private final CoretexAbstractItemTable itemTable;

	public CoretexSqlType(CoretexAbstractItemTable itemTable, StructKind kind, List<RelDataTypeField> fields, boolean nullable) {
		super(kind, fields, nullable);
		this.itemTable = itemTable;
	}

	public CoretexSqlType(CoretexAbstractItemTable itemTable, StructKind kind, List<RelDataTypeField> fields) {
		this(itemTable, kind, fields, false );
	}

	public CoretexSqlType(CoretexAbstractItemTable itemTable, List<RelDataTypeField> fields) {
		this(itemTable, StructKind.NONE, fields);
	}

	public RelDataType createWithNullability(boolean nullable) {
		if (nullable == this.isNullable()) {
			return this;
		}
		return new CoretexSqlType(this.getItemTable(), this.getStructKind(), this.getFieldList(), nullable);
	}

	public CoretexAbstractItemTable getItemTable() {
		return itemTable;
	}
}
