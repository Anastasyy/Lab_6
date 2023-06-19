package com.megateam.common.exception.impl.file;

import com.megateam.common.exception.FileException;

/** This exception is thrown if some file is not found */
public class FileNotExistsException extends FileException {
    /**
     * FileException constructor
     *
     * @param message message for the exception
     */
    public FileNotExistsException(String message) {
        super(message);
    }
}
