package com.coretex.build.builder.impl;

import com.coretex.build.exceptions.StructProcessorException;

public class StructFileValidationException extends StructProcessorException {
	public StructFileValidationException(String message) {
		super(message);
	}

	public StructFileValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
