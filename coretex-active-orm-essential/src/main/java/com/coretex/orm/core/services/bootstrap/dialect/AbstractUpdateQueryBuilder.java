package com.coretex.orm.core.services.bootstrap.dialect;

import com.coretex.orm.core.activeorm.query.operations.contexts.UpdateOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.UpdateValueDataHolder;
import com.coretex.orm.core.activeorm.query.specs.UpdateOperationSpec;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractUpdateQueryBuilder<CTX extends UpdateOperationConfigContext> implements QueryBuilder<CTX> {
	public final static String DEFAULT_UPDATE_ITEM_QUERY = "update %s set %s where uuid = :uuid";

	@Override
	public void prepareValueData(CTX context){
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();
		item.setUpdateDate(LocalDateTime.now());

		Map<String, UpdateValueDataHolder> saveColumnValues = operationSpec.getAllAttributes().entrySet().stream()
				.filter(entry -> !(entry.getValue().getAttributeType() instanceof MetaRelationTypeItem))
				.filter(entry -> StringUtils.isNoneBlank(entry.getValue().getColumnName()))
				.filter(entry -> item.getItemContext().isDirty(entry.getKey()) ||
						Objects.nonNull(entry.getValue().getDefaultValue()) ||
						!entry.getValue().getOptional())
				.collect(Collectors.toMap(entry -> entry.getValue().getColumnName(), entry -> createValueDataHolder(operationSpec, entry.getValue())));

		operationSpec.addValueDataHolders(saveColumnValues.values());

	}

	public UpdateValueDataHolder createValueDataHolder(UpdateOperationSpec operationSpec, MetaAttributeTypeItem attributeTypeItem) {
		return new UpdateValueDataHolder(attributeTypeItem, operationSpec);
	}
}
