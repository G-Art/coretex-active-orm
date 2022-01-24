package com.coretex.orm.core.activeorm.constraints.exceptions;

public abstract class ItemConstraintViolationException extends RuntimeException {

	public ItemConstraintViolationException() {
	}

	public ItemConstraintViolationException(String message) {
		super(message);
	}

	public ItemConstraintViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ItemConstraintViolationException(Throwable cause) {
		super(cause);
	}

	public ItemConstraintViolationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
