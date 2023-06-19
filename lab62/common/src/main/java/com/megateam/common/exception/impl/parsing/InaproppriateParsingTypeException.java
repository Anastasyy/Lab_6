package com.megateam.common.exception.impl.parsing;

import com.megateam.common.exception.ParsingException;

/** This exception is thrown if something went wrong during types parsing */
public class InaproppriateParsingTypeException extends ParsingException {
    /**
     * InaproppriateParsingException constructor
     *
     * @param message exception message
     */
    public InaproppriateParsingTypeException(String message) {
        super(message);
    }
}
