package com.megateam.common.exception.impl.validation;

import com.megateam.common.exception.ValidationException;

/** This exception is thrown if some parameter is empty */
public class CannotBeEmptyException extends ValidationException {
    /**
     * ValidationException constructor
     *
     * @param message exception message
     */
    public CannotBeEmptyException(String message) {
        super(message);
    }
}
