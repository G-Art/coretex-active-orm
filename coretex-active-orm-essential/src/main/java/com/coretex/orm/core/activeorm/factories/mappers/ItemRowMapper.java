package com.coretex.orm.core.activeorm.factories.mappers;

import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.general.utils.ItemUtils;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.context.factory.ItemContextFactory;
import com.coretex.orm.core.utils.TypeUtil;
import com.coretex.items.core.*;
import com.coretex.orm.meta.AbstractGenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class ItemRowMapper<T extends AbstractGenericItem> implements RowMapper<T> {
	private Logger LOG = LoggerFactory.getLogger(ItemRowMapper.class);

	private CoretexContext coretexContext;
	private ItemContextFactory itemContextFactory;
	private MetaAttributeTypeItem metaTypeAttributeTypeItem;
	private MetaAttributeTypeItem uuidAttributeTypeItem;

	public ItemRowMapper(CoretexContext coretexContext, ItemContextFactory itemContextFactory) {
		this.coretexContext = coretexContext;
		this.itemContextFactory = itemContextFactory;

		this.metaTypeAttributeTypeItem = coretexContext.findAttribute(GenericItem.ITEM_TYPE, GenericItem.META_TYPE);
		this.uuidAttributeTypeItem = coretexContext.findAttribute(GenericItem.ITEM_TYPE, GenericItem.UUID);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<String, ColumnValueProvider> mapOfColValues = new LinkedCaseInsensitiveMap<>(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			String key = JdbcUtils.lookupColumnName(rsmd, i);
			mapOfColValues.put(key, new ColumnValueProvider(rs, i));
		}

		MetaTypeItem typeMetaType = ((MetaTypeItem) mapOfColValues.get(metaTypeAttributeTypeItem.getColumnName()).apply(metaTypeAttributeTypeItem));
		Class<T> targetClass = typeMetaType.getItemClass();

		if (ItemUtils.isSystemType(typeMetaType)) {
			if (ItemUtils.isMetaAttributeType(typeMetaType)) {
				return (T) coretexContext.findMetaAttributeTypeItem((UUID) mapOfColValues.get(uuidAttributeTypeItem.getColumnName()).apply(uuidAttributeTypeItem));
			}
			return (T) coretexContext.findMetaType((UUID) mapOfColValues.get(uuidAttributeTypeItem.getColumnName()).apply(uuidAttributeTypeItem));
		}

		ItemContext item = itemContextFactory.create(targetClass, (UUID) mapOfColValues.get(uuidAttributeTypeItem.getColumnName()).apply(uuidAttributeTypeItem));

		coretexContext.getAllAttributes(typeMetaType.getTypeCode()).values().stream()
				.filter(metaAttributeTypeItem -> mapOfColValues.containsKey(metaAttributeTypeItem.getColumnName()))
				.filter(metaAttributeTypeItem -> AttributeTypeUtils.isRegularTypeAttribute(metaAttributeTypeItem) ||
						(AttributeTypeUtils.isItemAttribute(metaAttributeTypeItem) &&
								(((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getSubtypes() == null ||
										((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getSubtypes().isEmpty())))
				.forEach(metaAttributeTypeItem -> item.initValue(metaAttributeTypeItem.getAttributeName(), mapOfColValues.get(metaAttributeTypeItem.getColumnName()).apply(metaAttributeTypeItem)));
		return ItemUtils.createItem(targetClass, item);

	}


	private class ColumnValueProvider implements Function<MetaAttributeTypeItem, Object> {

		private ResultSet rs;
		private int index;

		private ColumnValueProvider(ResultSet rs, int index) {
			this.rs = rs;
			this.index = index;
		}

		@Override
		public Object apply(MetaAttributeTypeItem metaAttributeTypeItem) {
			try {

				if (metaAttributeTypeItem.getAttributeType() instanceof MetaTypeItem && Arrays.asList(MetaTypeItem.ITEM_TYPE, MetaRelationTypeItem.ITEM_TYPE).contains(((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getTypeCode())) {
					Object value = JdbcUtils.getResultSetValue(rs, index);
					return coretexContext.findMetaType((UUID) (value instanceof String ? UUID.fromString((String) value): value));
				}
				if (AttributeTypeUtils.isEnumTypeAttribute(metaAttributeTypeItem)) {
					Object value = JdbcUtils.getResultSetValue(rs, index);
					return coretexContext.findMetaEnumValueTypeItem(((MetaEnumTypeItem) metaAttributeTypeItem.getAttributeType()).getEnumClass(), (UUID) (value instanceof String ? UUID.fromString((String) value): value));
				}
				if (AttributeTypeUtils.isRegularTypeAttribute(metaAttributeTypeItem)) {
					var value = coretexContext.getTypeTranslator(((RegularTypeItem) metaAttributeTypeItem.getAttributeType()).getRegularClass()).read(rs, index);
					if(Objects.isNull(value) && Objects.nonNull(metaAttributeTypeItem.getDefaultValue())){
						return TypeUtil.toType(metaAttributeTypeItem.getDefaultValue(), (RegularTypeItem) metaAttributeTypeItem.getAttributeType());
					}
					return value;
				} else {
					Class<AbstractGenericItem> itemClass = ((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getItemClass();
					var value = JdbcUtils.getResultSetValue(rs, index);
					return Objects.nonNull(value) ?
							ItemUtils.createItem(itemClass, itemContextFactory.create(itemClass, (UUID) (value instanceof String ? UUID.fromString((String) value): value))) :
							null;
				}
			} catch (Exception e) {
				if (LOG.isDebugEnabled()) {
					LOG.error(String.format("Can't read column number %s", index), e);
				}
				try {
					return JdbcUtils.getResultSetValue(rs, index);
				} catch (SQLException e1) {
					throw new RuntimeException(e1);
				}
			}
		}
	}
}
