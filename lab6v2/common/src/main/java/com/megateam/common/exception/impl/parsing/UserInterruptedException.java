package com.megateam.common.exception.impl.parsing;

import com.megateam.common.exception.ParsingException;

/** This exception is thrown if user interrupted parsing */
public class UserInterruptedException extends ParsingException {
    /**
     * ParsingException constructor
     *
     * @param message message for the exception
     */
    public UserInterruptedException(String message) {
        super(message);
    }
}
