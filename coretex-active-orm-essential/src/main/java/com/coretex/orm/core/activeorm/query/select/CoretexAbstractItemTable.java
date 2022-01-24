package com.coretex.orm.core.activeorm.query.select;

import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Sets;
import org.apache.calcite.schema.TranslatableTable;
import org.apache.calcite.schema.impl.AbstractTable;

import java.util.Set;

public abstract class CoretexAbstractItemTable extends AbstractTable implements TranslatableTable {

	private final CoretexContext coretexContext;

	public CoretexAbstractItemTable(CoretexContext coretexContext) {
		this.coretexContext = coretexContext;
	}

	public CoretexContext getCoretexContext() {
		return coretexContext;
	}

	public abstract String getTableName();

	public abstract MetaTypeItem getMetaType();

	public abstract String getItemTypeCode();

	public Set<MetaTypeItem> getAllSubtypes() {
		return Sets.newHashSet();
	}
}
