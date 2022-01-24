package com.coretex.orm.core.activeorm.query.specs.select;

import com.coretex.orm.core.activeorm.query.operations.contexts.SelectOperationConfigContext;
import com.coretex.orm.core.activeorm.query.specs.SqlOperationSpec;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SelectOperationSpec extends SqlOperationSpec {

	private Optional<String> query;
	private Class<?> expectedResultType;
	private Map<String, Object> parameters;
	private ResultSetExtractor<?> customExtractor;
	private String transformedQuery;

	protected SelectOperationSpec(){

	}

	public SelectOperationSpec(String query) {
		this(query, Maps.newHashMap());
	}

	public SelectOperationSpec(String query, Map<String, Object> parameters) {
		this.parameters = parameters;
		this.query = Optional.of(query);
	}

	public SelectOperationSpec(String query, Map<String, Object> parameters, ResultSetExtractor<?> customExtractor) {
		this(query, parameters);
		this.customExtractor = customExtractor;
	}

	@Override
	public SelectOperationConfigContext createOperationContext() {
		return new SelectOperationConfigContext(this);
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void addQueryParameter(String key, Object value) {
		Assert.notNull(value, "Value is required, null given for key: " + key);
		if(Objects.isNull(parameters)){
			parameters = Maps.newHashMap();
		}
		this.parameters.put(key, value);
	}

	public void addQueryParameters(Map<String, ?> params) {
		params.forEach(this::addQueryParameter);
	}

	public Class<?> getExpectedResultType() {
		return expectedResultType;
	}

	public void setExpectedResultType(Class<?> expectedResultType) {
		this.expectedResultType = expectedResultType;
	}

	public ResultSetExtractor<?> getCustomExtractor() {
		return customExtractor;
	}

	protected void setCustomExtractor(ResultSetExtractor<?> customExtractor) {
		this.customExtractor = customExtractor;
	}

	public String getTransformedQuery() {
		return transformedQuery;
	}

	public void setTransformedQuery(String transformedQuery) {
		this.transformedQuery = transformedQuery;
	}

	public String getQuery() {
		return query.get();
	}

	@Override
	public String toString() {
		return "query: [" + this.getQuery() + "], query parameters: [" + this.getParameters() + "]";
	}

}
