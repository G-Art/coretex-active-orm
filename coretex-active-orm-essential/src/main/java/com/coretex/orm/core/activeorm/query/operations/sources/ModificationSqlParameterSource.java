package com.coretex.orm.core.activeorm.query.operations.sources;

import com.coretex.orm.core.activeorm.query.operations.dataholders.AbstractValueDataHolder;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.Assert;

import java.util.Map;

public class ModificationSqlParameterSource<H extends AbstractValueDataHolder> extends MapSqlParameterSource {

	public ModificationSqlParameterSource(String paramName, H value) {
		super(paramName, value);
	}

	public ModificationSqlParameterSource(Map<String, H> values) {
		super(values);
	}

	@Override
	public MapSqlParameterSource addValue(String paramName, Object value) {
		Assert.notNull(paramName, "Parameter name must not be a null");
		if (AbstractValueDataHolder.class.isAssignableFrom(value.getClass())) {
			return super.addValue(paramName, ((AbstractValueDataHolder) value).createSqlParameterValue());
		}

		return super.addValue(paramName, value);
	}

	@Override
	public MapSqlParameterSource addValues(Map<String, ?> values) {
		if (values != null) {
			for (Map.Entry<String, ?> entry : values.entrySet()) {
				this.addValue(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}
}
