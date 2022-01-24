package com.coretex.orm.core.activeorm.query.select;

import com.coretex.items.core.MetaTypeItem;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.schema.CustomColumnResolvingTable;
import org.apache.calcite.util.Pair;

import java.util.List;
import java.util.Set;

public class CoretexStrictItemTable extends CoretexAbstractItemTable implements CustomColumnResolvingTable {

	private final CoretexItemTable coretexItemTable;


	public CoretexStrictItemTable(CoretexItemTable coretexItemTable) {
		super(coretexItemTable.getCoretexContext());
		this.coretexItemTable = coretexItemTable;
	}

	@Override
	public RelDataType getRowType(RelDataTypeFactory typeFactory) {
		return coretexItemTable.getRowType(typeFactory);
	}

	@Override
	public Set<MetaTypeItem> getAllSubtypes() {
		return super.getAllSubtypes();
	}

	@Override
	public String getTableName() {
		return coretexItemTable.getTableName();
	}

	@Override
	public MetaTypeItem getMetaType() {
		return coretexItemTable.getMetaType();
	}

	@Override
	public String getItemTypeCode() {
		return coretexItemTable.getItemTypeCode();
	}

	public boolean isNativeTable() {
		return coretexItemTable.isNativeTable();
	}

	public boolean isHasNativeLocalizedTable() {
		return coretexItemTable.isHasNativeLocalizedTable();
	}

	@Override
	public RelNode toRel(RelOptTable.ToRelContext context, RelOptTable relOptTable) {
		return coretexItemTable.toRel(context, relOptTable);
	}

	@Override
	public List<Pair<RelDataTypeField, List<String>>> resolveColumn(RelDataType rowType, RelDataTypeFactory typeFactory, List<String> names) {
		return coretexItemTable.resolveColumn(rowType, typeFactory, names);
	}
}
