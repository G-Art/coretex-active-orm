package com.coretex.orm.core.activeorm.exceptions;

public class MandatoryAttributeException extends RuntimeException {

	public MandatoryAttributeException() {
	}

	public MandatoryAttributeException(String message) {
		super(message);
	}

	public MandatoryAttributeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MandatoryAttributeException(Throwable cause) {
		super(cause);
	}

	public MandatoryAttributeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
