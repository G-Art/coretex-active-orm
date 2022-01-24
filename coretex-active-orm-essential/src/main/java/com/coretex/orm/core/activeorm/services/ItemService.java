package com.coretex.orm.core.activeorm.services;

import com.coretex.items.core.GenericItem;
import com.coretex.orm.meta.AbstractGenericItem;

import java.util.Collection;
import java.util.UUID;

public interface ItemService {


	<T extends GenericItem> T create(Class<T> itemClass);

	@SuppressWarnings("unchecked")
	<T extends GenericItem> T create(String typeCode);

	<T extends GenericItem> T create(Class<T> itemClass, UUID uuid);

	<T extends GenericItem> T save(T item);

	<T extends GenericItem>  void saveAll(Collection<T> items);

	<T extends GenericItem>  void delete(T item);

	<T extends GenericItem>  void deleteAll(Collection<T> items);
}
