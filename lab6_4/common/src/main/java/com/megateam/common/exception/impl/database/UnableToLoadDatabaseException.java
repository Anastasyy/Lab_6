package com.megateam.common.exception.impl.database;

import com.megateam.common.exception.DatabaseException;

/** This exception is thrown if something went wrong during database loading */
public class UnableToLoadDatabaseException extends DatabaseException {
    /**
     * DatabaseException constructor
     *
     * @param message message for the exception
     */
    public UnableToLoadDatabaseException(String message) {
        super(message);
    }
}
