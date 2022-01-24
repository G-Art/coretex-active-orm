package com.coretex.orm.core.activeorm.exceptions;

public class SearchException extends RuntimeException {


	public SearchException(String message) {
		super(message);
	}

	public SearchException(String message, Throwable cause) {
		super(message, cause);
	}

	public SearchException(Throwable cause) {
		super(cause);
	}

	public SearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
