package com.coretex.orm.core.activeorm.query.select;

import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.items.core.MetaTypeItem;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.logical.LogicalTableScan;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rel.type.StructKind;
import org.apache.calcite.sql.type.SqlTypeName;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CoretexLocalizedItemTable extends CoretexAbstractItemTable {


	private final CoretexItemTable coretexItemTable;
	private RelDataType rowType;


	public CoretexLocalizedItemTable(CoretexItemTable coretexItemTable, CoretexContext coretexContext) {
		super(coretexContext);
		this.coretexItemTable = coretexItemTable;
	}


	@Override
	public RelDataType getRowType(RelDataTypeFactory typeFactory) {
		if (rowType == null) {
			List<RelDataTypeField> fields = new ArrayList<>(4);

			RelDataType fieldType = typeFactory.createSqlType(SqlTypeName.getNameForJdbcType(Types.VARCHAR));
			RelDataTypeField owner = new CoretexLocalizedTableRelDataTypeFieldImpl("owner", 0, fieldType);
			fields.add(owner);
			RelDataTypeField attr = new CoretexLocalizedTableRelDataTypeFieldImpl("attribute", 1, fieldType);
			fields.add(attr);
			RelDataTypeField localeiso = new CoretexLocalizedTableRelDataTypeFieldImpl("localeiso", 2, fieldType);
			fields.add(localeiso);
			RelDataTypeField val = new CoretexLocalizedTableRelDataTypeFieldImpl("value", 3, fieldType);
			fields.add(val);

			rowType = new CoretexSqlType(this, StructKind.NONE, fields);
		}

		return rowType;
	}

	@Override
	public String getTableName() {
		return coretexItemTable.getTableName() + "_loc";
	}

	@Override
	public MetaTypeItem getMetaType() {
		return coretexItemTable.getMetaType();
	}

	@Override
	public String getItemTypeCode() {
		return coretexItemTable.getItemTypeCode();
	}

	@Override
	public RelNode toRel(RelOptTable.ToRelContext context, RelOptTable relOptTable) {
		return LogicalTableScan.create(context.getCluster(), relOptTable, context.getTableHints());

	}
}
