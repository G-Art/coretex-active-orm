package com.coretex.orm.core.activeorm.query.select;

import com.coretex.items.core.MetaAttributeTypeItem;
import org.apache.calcite.rel.type.RelDataType;

public class CoretexRelDataTypeFieldImpl extends CoretexAbstractRelDataTypeFieldImpl {

	private final MetaAttributeTypeItem metaAttributeTypeItem;

	public CoretexRelDataTypeFieldImpl(MetaAttributeTypeItem metaAttributeTypeItem, int index, RelDataType type) {
		super(metaAttributeTypeItem.getAttributeName(), index, type);
		if(type instanceof CoretexBasicSqlType){
			((CoretexBasicSqlType)type).setFieldFor(this);
		}
		this.metaAttributeTypeItem = metaAttributeTypeItem;
	}

	public MetaAttributeTypeItem getMetaAttributeTypeItem() {
		return metaAttributeTypeItem;
	}

	@Override
	public String getColumnName(){
		return getMetaAttributeTypeItem().getColumnName().toLowerCase();
	}
}
