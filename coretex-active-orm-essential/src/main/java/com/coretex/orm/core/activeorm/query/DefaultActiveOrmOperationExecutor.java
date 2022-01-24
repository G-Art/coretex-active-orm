package com.coretex.orm.core.activeorm.query;

import com.coretex.orm.core.activeorm.query.operations.SqlOperation;
import com.coretex.orm.core.activeorm.query.operations.contexts.AbstractOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.suppliers.OperationSupplier;
import com.coretex.orm.core.activeorm.query.specs.*;
import com.coretex.orm.core.activeorm.services.ItemService;
import com.coretex.orm.core.activeorm.services.ReactiveSearchResult;
import com.coretex.orm.core.general.utils.OperationUtils;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import org.springframework.beans.factory.annotation.Lookup;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.coretex.orm.core.general.utils.OperationUtils.isLoopSafe;

public class DefaultActiveOrmOperationExecutor implements ActiveOrmOperationExecutor {

	private Map<QueryType, OperationSupplier<?, ? extends OperationConfigContext<?>>> activeOrmOperationMap;

	public DefaultActiveOrmOperationExecutor(Map<QueryType, OperationSupplier<?, ? extends OperationConfigContext<?>>> activeOrmOperationMap) {

		this.activeOrmOperationMap = activeOrmOperationMap;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T, R extends ReactiveSearchResult<T>, CTX extends OperationConfigContext<?>> R execute(CTX context) {
		OperationSupplier<?, CTX> operationSupplier = (OperationSupplier<?, CTX>) activeOrmOperationMap.get(context.getQueryType());
		SqlOperation<?, CTX> sqlOperation = (SqlOperation<?, CTX>) operationSupplier.get(context);
		var result = sqlOperation.<T>execute(context);
		return context.wrapResult(result);
	}


	@Override
	public <T extends GenericItem> boolean executeSaveOperation(T item) {

		if (isNew(item)) {
			execute(new InsertOperationSpec(item).createOperationContext());
		} else {
			execute(new UpdateOperationSpec(item).createOperationContext());
		}
		return true;
	}

	@Override
	public <T extends GenericItem> boolean executeSaveOperation(T item,
	                                                            MetaAttributeTypeItem attributeTypeItem,
	                                                            AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator) {

		if (attributeTypeItem.getLocalized()) {
			execute(new LocalizedDataSaveOperationSpec(initiator, attributeTypeItem).createOperationContext());
		} else {
			if (isNew(item)) {
				execute(new CascadeInsertOperationSpec(initiator, item, attributeTypeItem).createOperationContext());
			} else {
				execute(new CascadeUpdateOperationSpec(initiator, item, attributeTypeItem).createOperationContext());
			}
		}
		return true;
	}

	@Override
	public <T extends GenericItem> boolean executeRelationSaveOperations(Collection<T> items,
	                                                                     MetaAttributeTypeItem attributeTypeItem,
	                                                                     AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator) {
		ModificationOperationSpec initiatorOperationSpec = initiator.getOperationSpec();
		GenericItem ownerItem = initiatorOperationSpec.getItem();
		items.stream()
				.filter(Objects::nonNull)
				.filter(item -> OperationUtils.isLoopSafe(initiatorOperationSpec, item))
				.forEach(item -> {
					if (item.getItemContext().isDirty()) {
						executeSaveOperation(item, attributeTypeItem, initiator);
					}
					if (isNew(item) || !OperationUtils.haveRelation(item, ownerItem, attributeTypeItem)) {
						executeRelationSaveOperation(item, ownerItem, attributeTypeItem, initiator);
					}
				});
		return true;
	}

	@Override
	public <T extends GenericItem> boolean executeRelationSaveOperations(T item,
	                                                                     MetaAttributeTypeItem attributeTypeItem,
	                                                                     AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator) {

		return executeRelationSaveOperations(List.of(item), attributeTypeItem, initiator);
	}

	@Override
	public <T extends GenericItem> boolean executeDeleteOperation(T item) {
		execute(new RemoveOperationSpec(item).createOperationContext());
		return true;
	}

	public <T extends GenericItem> boolean executeDeleteOperation(T item,
	                                                              MetaAttributeTypeItem attributeTypeItem,
	                                                              AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator) {

		if (attributeTypeItem.getLocalized()) {
			execute(new LocalizedDataRemoveOperationSpec(initiator, attributeTypeItem).createOperationContext());
		} else {
			execute(new CascadeRemoveOperationSpec(initiator, item, attributeTypeItem).createOperationContext());
		}
		return true;
	}

	@Override
	public <T extends GenericItem> boolean executeRelationDeleteOperations(Collection<T> items,
	                                                                       MetaAttributeTypeItem attributeTypeItem,
	                                                                       AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator) {
		GenericItem ownerItem = initiator.getOperationSpec().getItem();
		items.stream().filter(Objects::nonNull).forEach(item -> {
			boolean loopSave = isLoopSafe(initiator.getOperationSpec(), item);
			if (attributeTypeItem.getAssociated() && loopSave) {
				executeDeleteOperation(item, attributeTypeItem, initiator);
			}
			if (loopSave) {
				executeRelationDeleteOperation(item, ownerItem, attributeTypeItem, initiator);
			}
		});
		return true;
	}

	@Override
	public <T extends GenericItem> boolean executeRelationDeleteOperations(T item,
	                                                                       MetaAttributeTypeItem attributeTypeItem,
	                                                                       AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator) {

		return executeRelationDeleteOperations(List.of(item), attributeTypeItem, initiator);
	}


	private <T extends GenericItem> boolean executeRelationDeleteOperation(T item,
	                                                                       GenericItem ownerItem,
	                                                                       MetaAttributeTypeItem attributeTypeItem,
	                                                                       AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator) {

		GenericItem relation = (GenericItem) getItemService().create(((MetaRelationTypeItem) attributeTypeItem.getAttributeType()).getItemClass());

		if (attributeTypeItem.getSource()) {
			setRelations(ownerItem, item, relation);
		} else {
			setRelations(item, ownerItem, relation);
		}

		return executeDeleteOperation(relation, attributeTypeItem, initiator);
	}

	private <T extends GenericItem> boolean executeRelationSaveOperation(T item,
	                                                                     GenericItem ownerItem,
	                                                                     MetaAttributeTypeItem attributeTypeItem,
	                                                                     AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator) {

		GenericItem relation = getItemService().create(((MetaRelationTypeItem) attributeTypeItem.getAttributeType()).getItemClass());

		LocalDateTime now = LocalDateTime.now();
		relation.setAttributeValue(GenericItem.CREATE_DATE, now);
		relation.setAttributeValue(GenericItem.UPDATE_DATE, now);

		if (attributeTypeItem.getSource()) {
			setRelations(ownerItem, item, relation);
		} else {
			setRelations(item, ownerItem, relation);
		}

		return executeSaveOperation(relation, attributeTypeItem, initiator);
	}

	private void setRelations(GenericItem source, GenericItem target, GenericItem relation) {
		relation.setAttributeValue("source", source);
		relation.setAttributeValue("sourceType", source.getMetaType());
		relation.setAttributeValue("target", target);
		relation.setAttributeValue("targetType", target.getMetaType());
	}

	private <T extends GenericItem> boolean isNew(T item) {
		return item.getItemContext().isNew() || !item.getItemContext().isExist();
	}

	@Lookup
	public ItemService getItemService() {
		return null;
	}
}
