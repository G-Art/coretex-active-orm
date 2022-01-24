package com.coretex.orm.core.activeorm.query.select;

import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.ImmutableMap;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.schema.impl.AbstractTable;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CoretexItemsSchema extends AbstractSchema {

	private final CoretexContext coretexContext;

	private @Nullable
	final Map<String, Table> tableMap;

	public CoretexItemsSchema(CoretexContext coretexContext) {
		this.coretexContext = coretexContext;

		Map<String, CoretexItemTable> nativeTables = coretexContext.getAllMetaTypes()
				.stream()
				.filter(MetaTypeItem::getTableOwner)
				.map(item -> createNativeTables(item, true))
				.collect(Collectors.toMap(
								CoretexItemTable::getTableName,
								Function.identity()
						)
				);

		Map<String, AbstractTable> nativeLocalizedTables = nativeTables.values()
				.stream()
				.filter(CoretexItemTable::isHasNativeLocalizedTable)
				.map(this::createNativeLocalizedTables)
				.collect(Collectors.toMap(
								CoretexLocalizedItemTable::getTableName,
								Function.identity()
						)
				);

		Map<String, CoretexItemTable> itemTables = coretexContext.getAllMetaTypes()
				.stream()
				.map(item -> createItemTables(item, false))
				.collect(Collectors.toMap(
								CoretexItemTable::getItemTypeCode,
								Function.identity()
						)
				);

		Map<String, CoretexStrictItemTable> itemTableStrict = itemTables.values()
				.stream()
				.collect(Collectors.toMap(coretexItemTable -> "#" + coretexItemTable.getItemTypeCode(),
						CoretexStrictItemTable::new));

		this.tableMap = ImmutableMap.<String, Table>builder()
				.putAll(itemTables)
				.putAll(itemTableStrict)
				.putAll(nativeTables)
				.putAll(nativeLocalizedTables)
				.build();

	}

	@Nullable
	@Override
	protected Map<String, Table> getTableMap() {
		return tableMap;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	public CoretexItemTable createItemTables(MetaTypeItem metaType, boolean nativeTable) {
		return new CoretexItemTable(metaType, coretexContext, nativeTable);
	}

	public CoretexItemTable createNativeTables(MetaTypeItem metaType, boolean nativeTable) {
		return new CoretexItemTable(metaType, coretexContext, nativeTable);
	}

	public CoretexLocalizedItemTable createNativeLocalizedTables(CoretexItemTable coretexItemTable) {
		return new CoretexLocalizedItemTable(coretexItemTable, coretexContext);
	}

}
