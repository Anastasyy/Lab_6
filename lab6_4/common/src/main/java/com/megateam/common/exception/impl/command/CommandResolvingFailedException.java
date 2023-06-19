package com.megateam.common.exception.impl.command;

import com.megateam.common.exception.CommandException;

/** This exception is thrown if single command resolving or command script resolving failed */
public class CommandResolvingFailedException extends CommandException {
    /**
     * CommandException constructor
     *
     * @param message exception message
     */
    public CommandResolvingFailedException(String message) {
        super(message);
    }
}
