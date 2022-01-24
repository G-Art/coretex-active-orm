package com.coretex.orm.core.activeorm.query.operations;

import com.coretex.orm.core.activeorm.query.QueryType;
import com.coretex.orm.core.activeorm.query.operations.contexts.RemoveOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.sources.ModificationSqlParameterSource;
import com.coretex.orm.core.activeorm.query.specs.CascadeRemoveOperationSpec;
import com.coretex.orm.core.activeorm.query.specs.RemoveOperationSpec;
import com.coretex.orm.core.activeorm.services.AbstractJdbcService;
import com.coretex.orm.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;

import static com.coretex.orm.core.general.utils.AttributeTypeUtils.isRegularTypeAttribute;
import static com.coretex.orm.core.general.utils.ItemUtils.isSystemType;
import static java.lang.String.format;

public class RemoveOperation extends ModificationOperation<RemoveOperationSpec, RemoveOperationConfigContext> {
	private Logger LOG = LoggerFactory.getLogger(RemoveOperation.class);

	public RemoveOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService, DbDialectService dbDialectService) {
		super(abstractJdbcService, itemOperationInterceptorService, dbDialectService);
	}


	@Override
	public QueryType getQueryType() {
		return QueryType.DELETE;
	}

	@Override
	protected void executeBefore(RemoveOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		if (operationSpec.isCascadeEnabled()) {
			operationSpec.getAllAttributes()
					.values()
					.stream()
					.filter(attributeTypeItem -> !isRegularTypeAttribute(attributeTypeItem) || attributeTypeItem.getLocalized())
					.forEach(attributeTypeItem -> {
						if (attributeTypeItem.getLocalized()) {
							getActiveOrmOperationExecutor().executeDeleteOperation(operationSpec.getItem(), attributeTypeItem, operationConfigContext);
						}
						if (attributeTypeItem.getAttributeTypeCode().equals(MetaTypeItem.ITEM_TYPE) && !isSystemType(attributeTypeItem.getAttributeType()) && attributeTypeItem.getAssociated()) {
							var attributeValue = operationSpec.getItem().getAttributeValue(attributeTypeItem.getAttributeName());
							if (Objects.nonNull(attributeValue)) {
								getActiveOrmOperationExecutor()
										.executeDeleteOperation((GenericItem) (attributeValue), attributeTypeItem, operationConfigContext);
							}
						}
						if (AttributeTypeUtils.isRelationAttribute(attributeTypeItem)) {
							Object value = operationSpec.getItem().getAttributeValue(attributeTypeItem.getAttributeName());
							if (Objects.nonNull(value)) {
								if (value instanceof Collection) {
									getActiveOrmOperationExecutor().executeRelationDeleteOperations((Collection<GenericItem>) value, attributeTypeItem, operationConfigContext);
								} else {
									getActiveOrmOperationExecutor().executeRelationDeleteOperations((GenericItem) value, attributeTypeItem, operationConfigContext);
								}
							}
						}
					});
		} else {
			LOG.warn("Cascade is switched off it may affect data consistent");
		}

	}

	@Override
	public void executeOperation(RemoveOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		var query = buildQuery(operationConfigContext);
		if (LOG.isDebugEnabled()) {
			LOG.debug(format("Execute query: [%s]; type: [%s]; cascade [%s]", query, getQueryType(), operationSpec instanceof CascadeRemoveOperationSpec));
		}
		executeJdbcOperation(jdbcTemplate -> jdbcTemplate.update(query,
				new ModificationSqlParameterSource<>(operationSpec.getValueDataHolders())));
	}

	@Override
	protected void executeAfter(RemoveOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		getItemOperationInterceptorService()
				.onRemove(operationSpec.getItem());
	}

	@Override
	protected boolean useInterceptors(RemoveOperationConfigContext operationConfigContext) {
		return false;
	}

	@Override
	protected boolean isTransactionInitiator() {
		return true;
	}

}
