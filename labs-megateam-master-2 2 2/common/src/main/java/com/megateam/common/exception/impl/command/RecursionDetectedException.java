package com.megateam.common.exception.impl.command;

import com.megateam.common.exception.CommandException;

/** This exception is thrown if recursion detected */
public class RecursionDetectedException extends CommandException {
    /**
     * RecursionDetectedException constructor
     *
     * @param message exception message
     */
    public RecursionDetectedException(String message) {
        super(message);
    }
}
