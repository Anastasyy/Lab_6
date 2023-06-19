package com.megateam.common.exception.impl.command;

import com.megateam.common.exception.CommandException;

/** This exception is thrown during resolving if default resolve(...) method is being used */
public class DefaultResolverException extends CommandException {
    /**
     * CommandException constructor
     *
     * @param message exception message
     */
    public DefaultResolverException(String message) {
        super(message);
    }
}
