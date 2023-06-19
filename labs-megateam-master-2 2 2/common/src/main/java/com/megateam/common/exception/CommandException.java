package com.megateam.common.exception;

/** This group of exceptions is thrown during command interaction */
public abstract class CommandException extends Exception {
    /**
     * CommandException constructor
     *
     * @param message exception message
     */
    public CommandException(String message) {
        super(message);
    }
}
