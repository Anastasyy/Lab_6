package com.megateam.common.exception.impl.validation;

import com.megateam.common.exception.ValidationException;

/** This exception is thrown if validation subject is out of bounds */
public class BoundValidationException extends ValidationException {
    /**
     * ValidationException constructor
     *
     * @param message exception message
     */
    public BoundValidationException(String message) {
        super(message);
    }
}
