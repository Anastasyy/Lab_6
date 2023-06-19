package com.megateam.common.exception.impl.command;

import com.megateam.common.exception.CommandException;

/** This exception is thrown during execution if default execute(...) method is being used */
public class DefaultExecutorException extends CommandException {
    /**
     * CommandException constructor
     *
     * @param message exception message
     */
    public DefaultExecutorException(String message) {
        super(message);
    }
}
