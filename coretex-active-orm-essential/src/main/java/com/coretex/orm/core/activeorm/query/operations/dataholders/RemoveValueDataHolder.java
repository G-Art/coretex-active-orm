package com.coretex.orm.core.activeorm.query.operations.dataholders;

import com.coretex.orm.core.activeorm.exceptions.QueryException;
import com.coretex.orm.core.activeorm.query.specs.RemoveOperationSpec;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumTypeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.coretex.orm.core.general.utils.ItemUtils.getTypeCode;

public class RemoveValueDataHolder extends AbstractValueDataHolder<RemoveOperationSpec> {

	private Logger LOG = LoggerFactory.getLogger(RemoveValueDataHolder.class);

	public RemoveValueDataHolder(MetaAttributeTypeItem attributeTypeItem, RemoveOperationSpec operationSpec) {
		super(operationSpec, attributeTypeItem);
	}

	public Object getValue(){
		if(getAttributeTypeItem().getAttributeType() instanceof MetaEnumTypeItem){
			return getMetaTypeProvider().findMetaEnumValueTypeItem((Enum)getItem().getAttributeValue(getAttributeTypeItem().getAttributeName())).getUuid();
		}
		if(isItemRelation()){
			if(getRelatedItem() == null){
				return null;
			}
			if(isMandatory() && getRelatedItem().getItemContext().isNew() && !getOperationSpec().isCascadeEnabled()){
				throw new QueryException(String.format("Related mandatory object [%s] for attribute [%s:%s] cant be removed due to cascade is off", getTypeCode(getRelatedItem()), getTypeCode(getItem()), getAttributeTypeItem().getAttributeName()));
			}
			if(!isMandatory() && getRelatedItem().getItemContext().isNew() && !getOperationSpec().isCascadeEnabled()){
				LOG.warn("Related object [%s] for attribute [%s:%s] will not be removed due to cascade is off");
				return null;
			}
			return getRelatedItem().getUuid();
		}

		return getItem().getAttributeValue(getAttributeTypeItem().getAttributeName());
	}

}
