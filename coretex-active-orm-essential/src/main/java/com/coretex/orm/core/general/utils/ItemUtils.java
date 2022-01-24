package com.coretex.orm.core.general.utils;

import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.exceptions.ItemCreationException;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public final class ItemUtils {

	private ItemUtils() {
	}

	public static <T extends AbstractGenericItem> T createItem(Class<T> itemClass, ItemContext ctx) {
		checkArgument(nonNull(itemClass), "Item class is expected");
		try {
			if (isNull(ctx)) {
				return ConstructorUtils.invokeConstructor(itemClass);
			}
			return ConstructorUtils.invokeConstructor(itemClass, ctx);
		} catch (ClassCastException | NoSuchMethodException | IllegalAccessException | InstantiationException |
				InvocationTargetException ex) {
			throw new ItemCreationException(ex.getMessage(), ex);
		}
	}

	public static Set<MetaTypeItem> getAllSubtypes(MetaTypeItem item){
		var subtypes = Sets.<MetaTypeItem>newHashSet();
		if(CollectionUtils.isNotEmpty(item.getSubtypes())){
			item.getSubtypes().forEach(subItem -> subtypes.addAll(getAllSubtypes(subItem)));
			subtypes.addAll(item.getSubtypes());
		}
		return subtypes;
	}

	public static <T extends AbstractGenericItem> T createItem(Class<T> itemClass) {
		return createItem(itemClass, null);
	}

	public static String getTypeCode(AbstractGenericItem genericItem){
		return genericItem.getItemContext().getTypeCode();
	}

	public static boolean isSystemType(MetaTypeItem item){
		return MetaTypeItem.class.isAssignableFrom(item.getItemClass()) || isMetaAttributeType(item);
	}

	public static boolean isSystemType(AbstractGenericItem item){
		return item instanceof MetaTypeItem && isSystemType((MetaTypeItem) item);
	}

	public static boolean isMetaAttributeType(MetaTypeItem item) {
		return MetaAttributeTypeItem.class.isAssignableFrom(item.getItemClass());
	}
}
