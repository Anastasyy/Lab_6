package com.megateam.common.exception.impl.command;

import com.megateam.common.exception.CommandException;

/** This exception is thrown if command not found during resolving */
public class CommandNotFoundException extends CommandException {
    /**
     * CommandException constructor
     *
     * @param message exception message
     */
    public CommandNotFoundException(String message) {
        super(message);
    }
}
