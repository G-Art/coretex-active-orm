package com.coretex.orm.core.activeorm.query.operations.dataholders;

import com.coretex.orm.core.activeorm.exceptions.QueryException;
import com.coretex.orm.core.activeorm.query.specs.UpdateOperationSpec;
import com.coretex.orm.core.utils.TypeUtil;
import com.coretex.items.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.coretex.orm.core.general.utils.ItemUtils.getTypeCode;

public class UpdateValueDataHolder extends AbstractValueDataHolder<UpdateOperationSpec> {

	private Logger LOG = LoggerFactory.getLogger(UpdateValueDataHolder.class);

	public UpdateValueDataHolder(MetaAttributeTypeItem attributeTypeItem, UpdateOperationSpec operationSpec) {
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
				throw new QueryException(String.format("Related mandatory object [%s] for attribute [%s:%s] cant be stored due to cascade save is off", getTypeCode(getRelatedItem()), getTypeCode(getItem()), getAttributeTypeItem().getAttributeName()));
			}
			if(!isMandatory() && getRelatedItem().getItemContext().isNew() && !getOperationSpec().isCascadeEnabled()){
				LOG.warn("Related object [%s] for attribute [%s:%s] will not be stored due to cascade save is off");
				return null;
			}
			return getRelatedItem().getUuid();
		}
		Object value = getItem().getAttributeValue(getAttributeTypeItem().getAttributeName());
		if(Objects.isNull(value) && Objects.nonNull(getAttributeTypeItem().getDefaultValue())){
			return TypeUtil.toType(getAttributeTypeItem().getDefaultValue(), (RegularTypeItem) getAttributeTypeItem().getAttributeType());
		}
		return value;
	}

}
