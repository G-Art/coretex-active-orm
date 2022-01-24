package com.coretex.orm.core.activeorm.query.operations;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.operations.contexts.UpdateOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.AbstractValueDataHolder;
import com.coretex.orm.core.activeorm.query.operations.sources.ModificationSqlParameterSource;
import com.coretex.orm.core.activeorm.query.specs.CascadeUpdateOperationSpec;
import com.coretex.orm.core.activeorm.query.specs.UpdateOperationSpec;
import com.coretex.orm.core.activeorm.services.AbstractJdbcService;
import com.coretex.orm.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.general.utils.OperationUtils;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.items.core.GenericItem;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UpdateOperation extends ModificationOperation<UpdateOperationSpec, UpdateOperationConfigContext> {

	private Logger LOG = LoggerFactory.getLogger(UpdateOperation.class);

	public UpdateOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService, DbDialectService dbDialectService) {
		super(abstractJdbcService, itemOperationInterceptorService, dbDialectService);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.UPDATE;
	}

	@Override
	protected void executeBefore(UpdateOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		if (operationSpec.isCascadeEnabled()) {
			operationSpec.getValueDataHolders()
					.values()
					.stream()
					.filter(AbstractValueDataHolder::isItemRelation)
					.forEach(valueDataHolder -> {
						if (Objects.isNull(valueDataHolder.getRelatedItem()) && valueDataHolder.getAttributeTypeItem().getAssociated()) {
							GenericItem val = operationSpec.getItem().getItemContext().getOriginValue(valueDataHolder.getAttributeTypeItem().getAttributeName());
							getActiveOrmOperationExecutor()
									.executeDeleteOperation(val, valueDataHolder.getAttributeTypeItem(), operationConfigContext);
						} else {
							if (valueDataHolder.availableForBeforeExecution()) {
								getActiveOrmOperationExecutor()
										.executeSaveOperation(valueDataHolder.getRelatedItem(), valueDataHolder.getAttributeTypeItem(), operationConfigContext);
							}
						}

					});
		} else {
			LOG.warn("Cascade is switched off it may affect data consistent");
		}
	}

	@Override
	public void executeOperation(UpdateOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		var query = buildQuery(operationConfigContext);
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Execute query: [%s]; type: [%s]; cascade [%s]", query, getQueryType(), operationSpec instanceof CascadeUpdateOperationSpec));
		}
		executeJdbcOperation(jdbcTemplate -> jdbcTemplate.update(query,
				new ModificationSqlParameterSource<>(operationSpec.getValueDataHolders())));
	}

	@Override
	protected void executeAfter(UpdateOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		getItemOperationInterceptorService()
				.onSaved(operationSpec.getItem());

		if (operationSpec.getHasLocalizedFields()) {
			operationSpec.getLocalizedFields().stream()
					.filter(attr -> operationSpec.getItem().getItemContext().isDirty(attr.getAttributeName()))
					.forEach(field -> getActiveOrmOperationExecutor().executeSaveOperation(null, field, operationConfigContext));
		}
		if (operationSpec.isCascadeEnabled()) {
			if (operationSpec.getHasRelationAttributes()) {
				operationSpec.getRelationAttributes().stream()
						.filter(attr -> operationSpec.getItem().getItemContext().isDirty(attr.getAttributeName()))
						.forEach(field -> {
							var value = operationSpec.getItem().getAttributeValue(field.getAttributeName());
							var originValue = operationSpec.getItem().getItemContext().getOriginValue(field.getAttributeName());
							if (AttributeTypeUtils.isCollection(field)) {
								Collection<GenericItem> values = (Collection<GenericItem>) value;
								Collection<GenericItem> originValues = (Collection<GenericItem>) originValue;

								List<GenericItem> toSave = Optional.ofNullable(values).map(val -> val.stream()
										.filter(it -> it.getItemContext().isNew()
												|| it.getItemContext().isDirty()
												|| !OperationUtils.haveRelation(it, operationSpec.getItem(), field))
										.collect(Collectors.toList())

								).orElseGet(Lists::newArrayList);
								if (CollectionUtils.isNotEmpty(toSave)) {
									getActiveOrmOperationExecutor().executeRelationSaveOperations(toSave, field, operationConfigContext);
								}

								List<GenericItem> toRemove = CollectionUtils.isEmpty(originValues) ? List.of() : List.copyOf(originValues);

								if (CollectionUtils.isNotEmpty(values)) {
									toRemove = Optional.ofNullable(originValues).map(val -> val.stream()
											.filter(it -> !values.contains(it)).collect(Collectors.toList())).orElseGet(Lists::newArrayList);
								}

								if (CollectionUtils.isNotEmpty(toRemove)) {
									getActiveOrmOperationExecutor().executeRelationDeleteOperations(toRemove, field, operationConfigContext);
								}

							} else {
								if (Objects.isNull(value) && Objects.nonNull(originValue)) {
									getActiveOrmOperationExecutor().executeRelationDeleteOperations((GenericItem) originValue, field, operationConfigContext);
								} else {
									if (Objects.nonNull(value)) {
										if (!value.equals(originValue) && Objects.nonNull(originValue)) {
											getActiveOrmOperationExecutor().executeRelationDeleteOperations((GenericItem) originValue, field, operationConfigContext);
										}
										getActiveOrmOperationExecutor().executeRelationSaveOperations((GenericItem) value, field, operationConfigContext);
									}
								}

							}
						});
			}
		} else {
			LOG.warn("Cascade is switched off it may affect data consistent");
		}
	}

	@Override
	protected boolean isTransactionInitiator() {
		return true;
	}
}
