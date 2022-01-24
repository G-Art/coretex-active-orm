package com.coretex.orm.core;

import java.util.Map;
import java.util.Optional;

public interface CoretexConfiguration {

	Map<String, ?> getConfigurations();
	<R> Optional<R> getConfig(String config, Class<R> resultType);
	<R> Optional<R> getConfig(String key, Class<R> type, R defaultValue);
}
