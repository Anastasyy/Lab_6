package com.megateam.common.exception.impl.command;

import com.megateam.common.exception.CommandException;

/** This exception is thrown if something went wrong during command execution */
public class CommandExecutionFailedException extends CommandException {
    /**
     * CommandException constructor
     *
     * @param message exception message
     */
    public CommandExecutionFailedException(String message) {
        super(message);
    }
}
