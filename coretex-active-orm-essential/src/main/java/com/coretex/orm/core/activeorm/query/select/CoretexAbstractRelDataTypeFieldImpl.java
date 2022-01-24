package com.coretex.orm.core.activeorm.query.select;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFieldImpl;

public class CoretexAbstractRelDataTypeFieldImpl extends RelDataTypeFieldImpl {


	public CoretexAbstractRelDataTypeFieldImpl(String name, int index, RelDataType type) {
		super(name, index, type);
	}

	public String getColumnName(){
		return getName();
	}
}
