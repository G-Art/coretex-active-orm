package com.coretex.orm.core;

import java.util.Map;
import java.util.Optional;

public class DefaultCoretexConfiguration implements CoretexConfiguration{



	@Override
	public Map<String, ?> getConfigurations() {
		return null;
	}

	@Override
	public <R> Optional<R> getConfig(String config, Class<R> resultType) {
		return Optional.empty();
	}

	@Override
	public <R> Optional<R> getConfig(String key, Class<R> type, R defaultValue) {
		return Optional.empty();
	}
}
