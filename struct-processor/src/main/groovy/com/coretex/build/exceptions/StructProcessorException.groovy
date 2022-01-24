package com.coretex.build.exceptions

class StructProcessorException extends RuntimeException {

	StructProcessorException(String message) {
		super(message)
	}

	StructProcessorException(String message, Throwable cause) {
		super(message, cause)
	}
}