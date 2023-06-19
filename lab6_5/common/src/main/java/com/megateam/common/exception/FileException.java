package com.megateam.common.exception;

/** This group of exceptions is thrown if something went wrong during file operations */
public abstract class FileException extends Exception {
    /**
     * FileException constructor
     *
     * @param message message for the exception
     */
    public FileException(String message) {
        super(message);
    }
}
