package com.coretex.orm.core.activeorm.query.specs.select;

import com.coretex.orm.core.utils.TypeUtil;
import com.coretex.items.core.GenericItem;

import java.util.Map;
import java.util.UUID;

public class SelectItemsOperationSpec<R extends GenericItem> extends SelectOperationSpec {

	private static final String SELECT_ITEM_BY_UUID_TEMPLATE = "select item.* from \"%s\" as item where item.uuid = :uuid";

	public SelectItemsOperationSpec(UUID uuid, Class<R> resultClass) {
		super(String.format(SELECT_ITEM_BY_UUID_TEMPLATE, TypeUtil.getMetaTypeCode(resultClass)), Map.of(GenericItem.UUID, uuid));
		setExpectedResultType(resultClass);
	}
}
