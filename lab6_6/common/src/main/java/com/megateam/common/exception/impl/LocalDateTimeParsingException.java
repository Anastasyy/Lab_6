package com.megateam.common.exception.impl;

import com.megateam.common.exception.ParsingException;

/** If the LocalDateTime cannot be parsed successfully, the exception is thrown */
public class LocalDateTimeParsingException extends ParsingException {
    /**
     * LocalDateTimeParsingException constructor
     *
     * @param message message for the exception
     */
    public LocalDateTimeParsingException(String message) {
        super(message);
    }
}
