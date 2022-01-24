package com.coretex.orm.core.services.bootstrap.impl;

import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumValueTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.core.RegularTypeItem;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface MetaTypeProvider {

	RegularTypeItem getRegularType(String typeCode);

	RegularTypeItem getRegularType(Class clazz);

	MetaTypeItem findMetaType(String typeCode);

	Set<MetaTypeItem> findMetaTypeForTable(String table);

	MetaTypeItem findMetaType(UUID typeCodeUUID);

	MetaAttributeTypeItem findMetaAttributeTypeItem(UUID typeCodeUUID);

	<E extends Enum> MetaEnumValueTypeItem findMetaEnumValueTypeItem(E enm);

	<E extends Enum> E findMetaEnumValueTypeItem(Class clazz, UUID uuid);

	default MetaAttributeTypeItem findAttribute(String typeCode, String attributeName) {
		return getAllAttributes(typeCode).get(attributeName);
	}

	Collection<MetaTypeItem> getAllMetaTypes();

	Map<String, MetaAttributeTypeItem> getAllAttributes(String metaTypeCode);

	default Map<String, MetaAttributeTypeItem> getAllAttributes(MetaTypeItem metaType) {
		return getAllAttributes(metaType.getTypeCode());
	}

	String getSqlTypeName(RegularTypeItem regularTypeItem);

	Integer getSqlType(RegularTypeItem regularTypeItem);

	void reload();
}
