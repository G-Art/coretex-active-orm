package com.coretex.orm.core.activeorm.query.select;

import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.general.utils.ItemUtils;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.logical.LogicalTableScan;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rel.type.StructKind;
import org.apache.calcite.schema.CustomColumnResolvingTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

public class CoretexItemTable extends CoretexAbstractItemTable implements CustomColumnResolvingTable {

	private final boolean nativeTable;
	private final boolean hasNativeLocalizedTable;
	private final MetaTypeItem metaType;
	private final String tableName;
	private final String itemTypeCode;
	private final Set<MetaTypeItem> allSubtypes;
	private final Map<String, MetaAttributeTypeItem> fieldNamesOwn;
	private final Map<String, MetaAttributeTypeItem> fieldNamesAll;
	private RelDataType rowType;

	public CoretexItemTable(MetaTypeItem metaType, CoretexContext coretexContext, boolean nativeTable) {
		super(coretexContext);
		this.nativeTable = nativeTable;
		this.itemTypeCode = metaType.getTypeCode();
		this.metaType = metaType;
		this.tableName = metaType.getTableName();
		this.fieldNamesAll = Maps.filterValues(
				getCoretexContext().getAllAttributes(this.itemTypeCode),
				metaAttributeTypeItem -> StringUtils.isNoneBlank(metaAttributeTypeItem.getColumnName())
		);
		this.fieldNamesOwn = Maps.filterKeys(
				this.fieldNamesAll,
				Predicates.in(
						Objects.nonNull(metaType.getItemAttributes()) ? metaType.getItemAttributes()
								.stream()
								.filter(metaAttributeTypeItem -> StringUtils.isNoneBlank(metaAttributeTypeItem.getColumnName()))
								.map(MetaAttributeTypeItem::getAttributeName)
								.collect(Collectors.toList()) : Sets.newHashSet()
				)
		);

		this.hasNativeLocalizedTable = coretexContext.getAllAttributes(this.itemTypeCode)
				.values()
				.stream()
				.anyMatch(MetaAttributeTypeItem::getLocalized);

		this.allSubtypes = ItemUtils.getAllSubtypes(this.metaType);
	}


	@Override
	public RelDataType getRowType(RelDataTypeFactory typeFactory) {
		if (rowType == null) {
			List<RelDataTypeField> fields = new ArrayList<>(fieldNamesAll.size());

			int i = 0;
			for (Map.Entry<String, MetaAttributeTypeItem> entry : fieldNamesAll.entrySet()) {
				String s = entry.getKey();
				MetaAttributeTypeItem metaAttributeTypeItem = entry.getValue();
				SqlTypeName sqlTypeName;
				if (AttributeTypeUtils.isRelationAttribute(metaAttributeTypeItem)) {
					continue;
				}
				Integer sqlType = getCoretexContext().getSqlType(AttributeTypeUtils.getRegularTypeForAttribute(metaAttributeTypeItem));

				switch (getCoretexContext().getDbDialectService().getDatabaseProduct()) {
					case MYSQL -> {
						if (sqlType == Types.LONGVARCHAR) {
							sqlTypeName = SqlTypeName.getNameForJdbcType(Types.VARCHAR);
						} else {
							sqlTypeName = SqlTypeName.getNameForJdbcType(sqlType == Types.OTHER ? Types.VARCHAR : sqlType);
						}
					}
					case POSTGRESQL -> sqlTypeName = SqlTypeName.getNameForJdbcType(sqlType == Types.OTHER ? Types.VARCHAR : sqlType);
					default -> sqlTypeName = SqlTypeName.getNameForJdbcType(Types.VARCHAR);
				}

				RelDataType fieldType = typeFactory.createSqlType(sqlTypeName);
				RelDataTypeField field = new CoretexRelDataTypeFieldImpl(metaAttributeTypeItem, i++, fieldType);
				fields.add(field);

			}

			rowType = new CoretexSqlType(this, StructKind.NONE, fields);
		}

		return rowType;
	}

	@Override
	public Set<MetaTypeItem> getAllSubtypes() {
		return allSubtypes;
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public MetaTypeItem getMetaType() {
		return metaType;
	}

	@Override
	public String getItemTypeCode() {
		return itemTypeCode;
	}

	public boolean isNativeTable() {
		return nativeTable;
	}

	public boolean isHasNativeLocalizedTable() {
		return hasNativeLocalizedTable;
	}

	@Override
	public RelNode toRel(RelOptTable.ToRelContext context, RelOptTable relOptTable) {
		return LogicalTableScan.create(context.getCluster(), relOptTable, context.getTableHints());
	}

	@Override
	public List<Pair<RelDataTypeField, List<String>>> resolveColumn(RelDataType rowType, RelDataTypeFactory typeFactory, List<String> names) {
		return rowType.getFieldList()
				.stream()
				.filter(relDataTypeField -> {
					if (relDataTypeField instanceof CoretexRelDataTypeFieldImpl) {
						return names.stream()
								.anyMatch(s -> s.equalsIgnoreCase(((CoretexRelDataTypeFieldImpl) relDataTypeField).getColumnName()) ||
										s.equalsIgnoreCase(relDataTypeField.getName())
								);
					}
					return names.stream().anyMatch(s -> s.equalsIgnoreCase(relDataTypeField.getName()));
				})
				.map(relDataTypeField -> Pair.of(relDataTypeField, names.stream()
								.filter(s -> relDataTypeField instanceof CoretexRelDataTypeFieldImpl ?
										!s.equalsIgnoreCase(relDataTypeField.getName()) &&
												!s.equalsIgnoreCase(((CoretexRelDataTypeFieldImpl) relDataTypeField).getColumnName()) :
										!s.equalsIgnoreCase(relDataTypeField.getName())
								)
								.collect(Collectors.toList())
						)
				).collect(Collectors.toList());
	}
}
