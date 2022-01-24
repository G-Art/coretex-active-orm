package com.coretex.orm.core.activeorm.query.rex;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rex.RexBuilder;

public class CoretexRexBuilder extends RexBuilder {
	/**
	 * Creates a RexBuilder.
	 *
	 * @param typeFactory Type factory
	 */
	public CoretexRexBuilder(RelDataTypeFactory typeFactory) {
		super(typeFactory);
	}

	public RexDynamicNamedParam makeDynamicNameParam(RelDataType type, String id, int index) {
		return new RexDynamicNamedParam(id, type, index);
	}

}
