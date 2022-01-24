package com.coretex.orm.core.activeorm.factories;

import com.coretex.orm.core.activeorm.exceptions.QueryException;
import com.coretex.orm.core.activeorm.factories.mappers.ItemRowMapper;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.orm.core.services.items.context.factory.ItemContextFactory;
import com.coretex.orm.meta.AbstractGenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.util.Map;

import static org.springframework.util.Assert.notNull;

public class RowMapperFactory {
	private Logger LOG = LoggerFactory.getLogger(RowMapperFactory.class);

	private CoretexContext coretexContext;

	private ItemContextFactory itemContextFactory;

	public <T> RowMapper<T> createMapper(Class<T> targetClass) {
		notNull(targetClass, "Generic type cant be recognized");
		if(AbstractGenericItem.class.isAssignableFrom(targetClass)){
			return createItemMapper(targetClass);
		}
		if(Map.class.isAssignableFrom(targetClass)){
			return createMapMapper();
		}
		throw new QueryException("Result cant be converted to " + targetClass.getName());
	}

	protected <T> RowMapper<T> createMapMapper() {
		return (RowMapper<T>) new ColumnMapRowMapper();
	}

	protected <T> RowMapper<T> createItemMapper(Class<? super T> type) {
		return new ItemRowMapper(coretexContext, itemContextFactory);
	}

	public void setCoretexContext(CoretexContext coretexContext) {
		this.coretexContext = coretexContext;
	}

	public void setItemContextFactory(ItemContextFactory itemContextFactory) {
		this.itemContextFactory = itemContextFactory;
	}

}
