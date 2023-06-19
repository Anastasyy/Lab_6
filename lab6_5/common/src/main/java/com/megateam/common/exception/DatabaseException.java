package com.megateam.common.exception;

/** This group of exceptions is thrown if something went wrong during the database operations */
public abstract class DatabaseException extends Exception {
    /**
     * DatabaseException constructor
     *
     * @param message message for the exception
     */
    public DatabaseException(String message) {
        super(message);
    }
}
