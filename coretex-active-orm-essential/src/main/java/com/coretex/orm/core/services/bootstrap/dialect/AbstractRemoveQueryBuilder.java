package com.coretex.orm.core.services.bootstrap.dialect;

import com.coretex.orm.core.activeorm.query.operations.contexts.RemoveOperationConfigContext;
import com.coretex.orm.core.activeorm.query.operations.dataholders.RemoveValueDataHolder;
import com.coretex.orm.core.activeorm.query.specs.RemoveOperationSpec;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;

import static com.coretex.orm.core.general.utils.ItemUtils.getTypeCode;

public abstract class AbstractRemoveQueryBuilder<CTX extends RemoveOperationConfigContext> implements QueryBuilder<CTX> {
	public final static String DEFAULT_DELETE_ITEM_QUERY = "delete from %s where %s";

	@Override
	public void prepareValueData(CTX context){
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();
		var metaTypeProvider = operationSpec.getMetaTypeProvider();
		if(item.getMetaType() instanceof MetaRelationTypeItem){
			MetaAttributeTypeItem sourceAttribute = metaTypeProvider.findAttribute(getTypeCode(item), "source");
			MetaAttributeTypeItem targetAttribute = metaTypeProvider.findAttribute(getTypeCode(item), "target");

			operationSpec.addValueDataHolder(createValueDataHolder(sourceAttribute, operationSpec));
			operationSpec.addValueDataHolder(createValueDataHolder(targetAttribute, operationSpec));
		}else{
			operationSpec.addValueDataHolder(createValueDataHolder(metaTypeProvider.findAttribute(getTypeCode(item), AbstractGenericItem.UUID), operationSpec));
		}
	}

	public RemoveValueDataHolder createValueDataHolder( MetaAttributeTypeItem attributeTypeItem, RemoveOperationSpec operationSpec) {
		return new RemoveValueDataHolder(attributeTypeItem, operationSpec);
	}
}
