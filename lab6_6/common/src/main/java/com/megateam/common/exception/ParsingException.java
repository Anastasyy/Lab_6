package com.megateam.common.exception;

/** This group of exception is thrown if something went wrong during parsing some data */
public abstract class ParsingException extends Exception {
    /**
     * ParsingException constructor
     *
     * @param message message for the exception
     */
    public ParsingException(String message) {
        super(message);
    }
}
