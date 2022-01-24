package com.coretex.orm.core.activeorm.exceptions;

public class AmbiguousResultException extends RuntimeException {

	public AmbiguousResultException() {
		super();
	}

	public AmbiguousResultException(String message) {
		super(message);
	}

	public AmbiguousResultException(String message, Throwable cause) {
		super(message, cause);
	}

	public AmbiguousResultException(Throwable cause) {
		super(cause);
	}

	protected AmbiguousResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
