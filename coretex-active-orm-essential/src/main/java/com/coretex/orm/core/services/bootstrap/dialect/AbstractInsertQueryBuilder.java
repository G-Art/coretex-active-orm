package com.coretex.orm.core.services.bootstrap.dialect;

import com.coretex.orm.core.activeorm.query.operations.contexts.InsertOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.InsertValueDataHolder;
import com.coretex.orm.core.activeorm.query.specs.InsertOperationSpec;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.coretex.orm.core.general.utils.ItemUtils.getTypeCode;

public abstract class AbstractInsertQueryBuilder<CTX extends InsertOperationConfigContext> implements QueryBuilder<CTX> {
	public final static String DEFAULT_INSERT_ITEM_QUERY = "insert into %s (%s) values (%s)";

	@Override
	public void prepareValueData(CTX context){
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();
		var metaTypeProvider = operationSpec.getMetaTypeProvider();

		LocalDateTime creationDate = LocalDateTime.now();
		item.setCreateDate(creationDate);
		item.setUpdateDate(creationDate);

		Map<String, InsertValueDataHolder> saveColumnValues = operationSpec.getAllAttributes().entrySet().stream()
				.filter(entry -> !(entry.getValue().getAttributeType() instanceof MetaRelationTypeItem))
				.filter(entry -> StringUtils.isNoneBlank(entry.getValue().getColumnName()))
				.filter(entry -> item.getItemContext().isDirty(entry.getKey()) ||
						Objects.nonNull(entry.getValue().getDefaultValue()) ||
						!entry.getValue().getOptional())
				.collect(Collectors.toMap(entry -> entry.getValue().getColumnName(), entry -> createInsertValueDataHolder(operationSpec, entry.getValue())));

		saveColumnValues.put(AbstractGenericItem.UUID, createInsertValueDataHolder(operationSpec, metaTypeProvider.findAttribute(getTypeCode(item), AbstractGenericItem.UUID)));

		operationSpec.addValueDataHolders(saveColumnValues.values());
	}

	public InsertValueDataHolder createInsertValueDataHolder(InsertOperationSpec operationSpec, MetaAttributeTypeItem attributeTypeItem) {
		return new InsertValueDataHolder(attributeTypeItem, operationSpec);
	}


}
