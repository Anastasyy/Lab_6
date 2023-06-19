package com.megateam.common.exception.impl.database;

import com.megateam.common.exception.DatabaseException;

/** This exception is thrown during the database saving operation if something went wrong */
public class UnableToSaveDatabaseException extends DatabaseException {
    /**
     * DatabaseException constructor
     *
     * @param message message for the exception
     */
    public UnableToSaveDatabaseException(String message) {
        super(message);
    }
}
