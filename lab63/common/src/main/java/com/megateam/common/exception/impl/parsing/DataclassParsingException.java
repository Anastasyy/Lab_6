package com.megateam.common.exception.impl.parsing;

import com.megateam.common.exception.ParsingException;

/** This exception is thrown if something went wrong during dataclass parsing */
public class DataclassParsingException extends ParsingException {
    /**
     * ParsingException constructor
     *
     * @param message message for the exception
     */
    public DataclassParsingException(String message) {
        super(message);
    }
}
