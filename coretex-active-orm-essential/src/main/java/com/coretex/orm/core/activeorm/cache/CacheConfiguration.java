package com.coretex.orm.core.activeorm.cache;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class CacheConfiguration {

	private boolean softValue = true;
	private long maximumSize = 512;
	private int concurrencyLevel = 2;
	private Duration duration = Duration.of(30, ChronoUnit.MINUTES);

	public void setSoftValue(boolean softValue) {
		this.softValue = softValue;
	}

	public boolean isSoftValue() {
		return softValue;
	}

	public long getMaximumSize() {
		return maximumSize;
	}

	public void setMaximumSize(long maximumSize) {
		this.maximumSize = maximumSize;
	}

	public int getConcurrencyLevel() {
		return concurrencyLevel;
	}

	public void setConcurrencyLevel(int concurrencyLevel) {
		this.concurrencyLevel = concurrencyLevel;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}
}
