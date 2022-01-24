package com.coretex.orm.core.activeorm.services.impl;

import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.orm.core.activeorm.query.ActiveOrmOperationExecutor;
import com.coretex.orm.core.activeorm.services.ItemService;
import com.coretex.orm.core.services.bootstrap.impl.MetaTypeProvider;
import com.coretex.orm.core.services.items.context.factory.ItemContextFactory;
import com.coretex.orm.core.services.items.exceptions.ItemCreationException;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.UUID;

import static org.springframework.util.Assert.notNull;

public class DefaultItemService implements ItemService {

	private final ItemContextFactory itemContextFactory;

	private final MetaTypeProvider metaTypeProvider;

	private final ActiveOrmOperationExecutor activeOrmOperationExecutor;

	public DefaultItemService(ItemContextFactory itemContextFactory, MetaTypeProvider metaTypeProvider, ActiveOrmOperationExecutor activeOrmOperationExecutor) {
		this.itemContextFactory = itemContextFactory;
		this.metaTypeProvider = metaTypeProvider;
		this.activeOrmOperationExecutor = activeOrmOperationExecutor;
	}

	@Override
	public <T extends GenericItem>  T create(Class<T> itemClass){
		try {
			return ConstructorUtils.invokeConstructor(itemClass, itemContextFactory.create(itemClass));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			throw new ItemCreationException("Can't create item", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends GenericItem> T create(String typeCode){
		try {
			MetaTypeItem metaTypeItem = metaTypeProvider.findMetaType(typeCode);
			notNull(metaTypeItem, String.format("Cant find type with type code: %s", typeCode));
			return ConstructorUtils.invokeConstructor((Class<T>) metaTypeItem.getItemClass(), itemContextFactory.create(typeCode));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			throw new ItemCreationException(String.format("Can't create item: %s", typeCode), e);
		}
	}

	@Override
	public <T extends GenericItem> T create(Class<T> itemClass, UUID uuid){
		try {
			return ConstructorUtils.invokeConstructor(itemClass, itemContextFactory.create(itemClass, uuid));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			throw new ItemCreationException("Can't create item", e);
		}
	}

	@Override
	public <T extends GenericItem> T save(T item){
		activeOrmOperationExecutor.executeSaveOperation(item);
		return item;
	}


	@Override
	public <T extends GenericItem> void saveAll(Collection<T> items){
		items.forEach(this::save);
	}

	@Override
	public <T extends GenericItem> void delete(T item){
		item.getItemContext().refresh();
		activeOrmOperationExecutor.executeDeleteOperation(item);
	}

	@Override
	public <T extends GenericItem> void deleteAll(Collection<T> items){
		items.forEach(this::delete);
	}
}
