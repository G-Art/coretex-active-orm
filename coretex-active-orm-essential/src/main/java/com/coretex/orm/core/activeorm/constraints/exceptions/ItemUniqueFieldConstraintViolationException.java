package com.coretex.orm.core.activeorm.constraints.exceptions;

public class ItemUniqueFieldConstraintViolationException extends ItemConstraintViolationException {

	public ItemUniqueFieldConstraintViolationException() {
	}

	public ItemUniqueFieldConstraintViolationException(String message) {
		super(message);
	}

	public ItemUniqueFieldConstraintViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ItemUniqueFieldConstraintViolationException(Throwable cause) {
		super(cause);
	}

	public ItemUniqueFieldConstraintViolationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
