package com.coretex.build.exceptions


class ItemBuildException extends StructProcessorException {

    ItemBuildException(String message) {
        super(message)
    }

    ItemBuildException(String message, Throwable cause) {
        super(message, cause)
    }
}
