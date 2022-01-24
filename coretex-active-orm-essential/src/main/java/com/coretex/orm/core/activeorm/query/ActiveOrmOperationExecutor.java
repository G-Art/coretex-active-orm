package com.coretex.orm.core.activeorm.query;

import com.coretex.orm.core.activeorm.query.operations.contexts.AbstractOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.orm.core.activeorm.query.specs.ModificationOperationSpec;
import com.coretex.orm.core.activeorm.services.ReactiveSearchResult;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;

import java.util.Collection;

public interface ActiveOrmOperationExecutor {

	<T, R extends ReactiveSearchResult<T>, CTX extends OperationConfigContext<?>> R execute(CTX context);

	<T extends GenericItem> boolean executeSaveOperation(T item);

	<T extends GenericItem> boolean executeSaveOperation(T item, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator);

	<T extends GenericItem> boolean executeRelationSaveOperations(Collection<T> items, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator);

	<T extends GenericItem> boolean executeRelationSaveOperations(T items, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator);

	<T extends GenericItem> boolean executeDeleteOperation(T item);

	<T extends GenericItem> boolean executeDeleteOperation(T item, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator);

	<T extends GenericItem> boolean executeRelationDeleteOperations(Collection<T> items, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator);

	<T extends GenericItem> boolean executeRelationDeleteOperations(T item, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<? extends ModificationOperationSpec> initiator);

}
