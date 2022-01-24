package com.coretex.orm.core.activeorm.query.operations;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.operations.contexts.InsertOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.AbstractValueDataHolder;
import com.coretex.orm.core.activeorm.query.operations.sources.ModificationSqlParameterSource;
import com.coretex.orm.core.activeorm.query.specs.CascadeInsertOperationSpec;
import com.coretex.orm.core.activeorm.query.specs.InsertOperationSpec;
import com.coretex.orm.core.activeorm.services.AbstractJdbcService;
import com.coretex.orm.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.items.core.GenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;

public class InsertOperation extends ModificationOperation<
		InsertOperationSpec,
		InsertOperationConfigContext> {

	private Logger LOG = LoggerFactory.getLogger(InsertOperation.class);

	public InsertOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService, DbDialectService dbDialectService) {
		super(abstractJdbcService, itemOperationInterceptorService, dbDialectService);
	}

	@Override
	protected void executeBefore(InsertOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		if (operationSpec.isCascadeEnabled()) {
			operationSpec.getValueDataHolders()
					.values()
					.stream()
					.filter(AbstractValueDataHolder::isItemRelation)
					.filter(AbstractValueDataHolder::availableForBeforeExecution)
					.forEach(insertValueDataHolder -> getActiveOrmOperationExecutor()
							.executeSaveOperation(
									insertValueDataHolder.getRelatedItem(),
									insertValueDataHolder.getAttributeTypeItem(),
									operationConfigContext));
		} else {
			LOG.warn("Cascade is switched off it may affect data consistent");
		}
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.INSERT;
	}

	@Override
	public void executeOperation(InsertOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		var query = buildQuery(operationConfigContext);
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Execute query: [%s]; type: [%s]; cascade [%s]", query, getQueryType(), operationSpec instanceof CascadeInsertOperationSpec));
		}
		executeJdbcOperation(jdbcTemplate -> jdbcTemplate.update(query,
				new ModificationSqlParameterSource<>(operationSpec.getValueDataHolders())));
	}

	@Override
	protected void executeAfter(InsertOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		operationSpec.getItem().setAttributeValue(GenericItem.UUID, operationSpec.getNewUuid());
		getItemOperationInterceptorService()
				.onSaved(operationSpec.getItem());

		if (operationSpec.getHasLocalizedFields()) {
			operationSpec.getLocalizedFields().stream()
					.filter(attr -> operationSpec.getItem().getItemContext().isDirty(attr.getAttributeName()))
					.forEach(field -> getActiveOrmOperationExecutor().executeSaveOperation(null, field, operationConfigContext));
		}

		if (operationSpec.getHasRelationAttributes()) {
			operationSpec.getRelationAttributes().stream()
					.filter(attr -> operationSpec.getItem().getItemContext().isDirty(attr.getAttributeName()))
					.forEach(field -> {
						Object value = operationSpec.getItem().getAttributeValue(field.getAttributeName());
						if (Objects.nonNull(value)) {
							if (value instanceof Collection) {
								getActiveOrmOperationExecutor().executeRelationSaveOperations((Collection<GenericItem>) value, field, operationConfigContext);
							} else {
								getActiveOrmOperationExecutor().executeRelationSaveOperations((GenericItem) value, field, operationConfigContext);
							}
						}
					});
		}
	}




	@Override
	protected boolean isTransactionInitiator() {
		return true;
	}
}
