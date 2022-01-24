package com.coretex.orm.core.services.exceptions;

public class CoreServiceException extends RuntimeException {

	public CoreServiceException(String message) {
		super(message);
	}

	public CoreServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
