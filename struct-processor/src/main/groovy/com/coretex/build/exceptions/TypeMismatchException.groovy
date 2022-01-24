package com.coretex.build.exceptions

class TypeMismatchException extends ItemBuildException {

    TypeMismatchException(String message) {
        super(message)
    }

    TypeMismatchException(String message, Throwable cause) {
        super(message, cause)
    }
}
