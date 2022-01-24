package com.coretex.orm.core.general.utils;

import com.coretex.orm.core.activeorm.query.specs.CascadeModificationOperationSpec;
import com.coretex.orm.core.activeorm.query.specs.ModificationOperationSpec;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;

import java.util.Collection;
import java.util.Objects;

public final class OperationUtils {

	private OperationUtils() {
	}

	public static  <T extends AbstractGenericItem> boolean haveRelation(T item, AbstractGenericItem ownerItem, MetaAttributeTypeItem attributeTypeItem) {
		if (AttributeTypeUtils.isCollection(attributeTypeItem)) {
			Collection<T> savedItems = ownerItem.getItemContext().getOriginValue(attributeTypeItem.getAttributeName());
			return Objects.nonNull(savedItems) && savedItems.contains(item);
		} else {
			T savedItem = ownerItem.getItemContext().getOriginValue(attributeTypeItem.getAttributeName());
			return Objects.nonNull(savedItem) && Objects.nonNull(savedItem.getUuid()) && savedItem.getUuid().equals(item.getUuid());
		}
	}

	public static  <T extends ModificationOperationSpec>  boolean isLoopSafe(T operationSpec, AbstractGenericItem item) {
		if(operationSpec instanceof CascadeModificationOperationSpec){
			return !((CascadeModificationOperationSpec) operationSpec).existInCascade(item);
		}else {
			return operationSpec.getItem() != item;
		}
	}
}
