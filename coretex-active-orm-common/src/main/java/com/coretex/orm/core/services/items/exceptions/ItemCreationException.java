package com.coretex.orm.core.services.items.exceptions;

import com.coretex.orm.core.services.exceptions.CoreServiceException;

public class ItemCreationException extends CoreServiceException {

	public ItemCreationException(String message) {
		super(message);
	}

	public ItemCreationException(String message, Throwable cause) {
		super(message, cause);
	}
}
