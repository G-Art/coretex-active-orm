package com.coretex.orm.core.activeorm.query;

import com.coretex.orm.core.activeorm.query.rex.CoretexRexBuilder;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.prepare.Prepare;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexDynamicParam;
import org.apache.calcite.sql.SqlDynamicParam;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql2rel.SqlRexConvertletTable;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.util.Util;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CoretexSqlToRelConverter extends SqlToRelConverter {

	private final Map<String, SqlDynamicParam> dynamicNamedParamSqlNodes = new HashMap<>();
	protected final CoretexRexBuilder rexBuilder;
	private final SqlValidator validator;

	public CoretexSqlToRelConverter(RelOptTable.ViewExpander viewExpander,
	                                @Nullable SqlValidator validator,
	                                Prepare.CatalogReader catalogReader,
	                                RelOptCluster cluster,
	                                SqlRexConvertletTable convertletTable,
	                                Config config) {
		super(viewExpander, validator, catalogReader, cluster, convertletTable, config);
		this.rexBuilder = (CoretexRexBuilder) cluster.getRexBuilder();
		this.validator = validator;
	}

	@Override
	public RexDynamicParam convertDynamicParam(
			final SqlDynamicParam dynamicParam) {

		if(dynamicParam instanceof SqlDynamicNamedParam){
			dynamicNamedParamSqlNodes.put(
					((SqlDynamicNamedParam) dynamicParam).getId(),
					dynamicParam);

			return rexBuilder.makeDynamicNameParam(
					getDynamicNamedParamType(((SqlDynamicNamedParam) dynamicParam).getId()),
					((SqlDynamicNamedParam) dynamicParam).getId(),
					dynamicParam.getIndex());
		}
		return super.convertDynamicParam(dynamicParam);

	}

	public RelDataType getDynamicNamedParamType(String id) {
		SqlNode sqlNode = dynamicNamedParamSqlNodes.get(id);
		if (sqlNode == null) {
			throw Util.needToImplement("dynamic named param type inference");
		}
		return validator.getValidatedNodeType(sqlNode);
	}


}
