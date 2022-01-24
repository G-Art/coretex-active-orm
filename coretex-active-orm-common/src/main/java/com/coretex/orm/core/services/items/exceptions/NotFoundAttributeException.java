package com.coretex.orm.core.services.items.exceptions;

import com.coretex.orm.core.services.exceptions.CoreServiceException;

public class NotFoundAttributeException extends CoreServiceException {

	public NotFoundAttributeException(String message) {
		super(message);
	}
}
