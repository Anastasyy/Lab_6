package com.megateam.common.exception;

/** This group of exceptions is thrown if validation not succeed */
public abstract class ValidationException extends Exception {
    /**
     * ValidationException constructor
     *
     * @param message exception message
     */
    public ValidationException(String message) {
        super(message);
    }
}
