package com.megateam.common.exception.impl.validation;

import com.megateam.common.exception.ValidationException;

/** This exception is thrown if validation subject is null */
public class NullValidationException extends ValidationException {
    /**
     * ValidationException constructor
     *
     * @param message exception message
     */
    public NullValidationException(String message) {
        super(message);
    }
}
