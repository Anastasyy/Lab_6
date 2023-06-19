package com.megateam.common.exception.impl.database;

import com.megateam.common.exception.DatabaseException;

/** This exception is thrown if you are trying to add the new element with an already existing id */
public class ElementIdAlreadyExistsException extends DatabaseException {
    /**
     * DatabaseException constructor
     *
     * @param message message for the exception
     */
    public ElementIdAlreadyExistsException(String message) {
        super(message);
    }
}
