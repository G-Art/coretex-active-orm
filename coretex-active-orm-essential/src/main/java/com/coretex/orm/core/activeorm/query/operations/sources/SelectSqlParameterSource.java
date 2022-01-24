package com.coretex.orm.core.activeorm.query.operations.sources;

import com.coretex.orm.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.items.core.GenericItem;
import com.coretex.orm.meta.AbstractGenericItem;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SelectSqlParameterSource extends MapSqlParameterSource {
	private SelectOperationSpec selectOperationSpec;
	private CoretexContext coretexContext;

	public SelectSqlParameterSource(SelectOperationSpec selectOperationSpec, CoretexContext coretexContext) {
		this.selectOperationSpec = selectOperationSpec;
		this.coretexContext = coretexContext;
		addValues(selectOperationSpec.getParameters());
	}

	@Override
	public MapSqlParameterSource addValue(String paramName, @Nullable Object value) {
		if(value instanceof GenericItem){
			if(Objects.isNull(((GenericItem) value).getUuid())){
				throw new NullPointerException(String.format("Item type [%s] for parameter [%s] has no uuid", ((GenericItem) value).getMetaType().getTypeCode(), paramName));
			}
			return super.addValue(paramName, ((AbstractGenericItem) value).getUuid(), coretexContext.getSqlType(coretexContext.getRegularType(UUID.class)));
		}
		if(value instanceof Enum){
			var metaEnumValueTypeItem = coretexContext.findMetaEnumValueTypeItem((Enum) value);
			if(Objects.nonNull(metaEnumValueTypeItem)){
				return super.addValue(paramName, metaEnumValueTypeItem.getUuid(), coretexContext.getSqlType(coretexContext.getRegularType(UUID.class)));
			}
			return super.addValue(paramName, value.toString(), coretexContext.getSqlType(coretexContext.getRegularType(String.class)));
		}
		return super.addValue(paramName, value, coretexContext.getSqlType(coretexContext.getRegularType(value.getClass())));
	}

	public MapSqlParameterSource addValues(@Nullable Map<String, ?> values) {
		if (values != null) {
			values.forEach(this::addValue);
		}
		return this;
	}
}
