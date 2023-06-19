package com.megateam.common.exception.impl.file;

import com.megateam.common.exception.FileException;

/** This exception is thrown if some file is not readable */
public class FileNotReadableException extends FileException {
    /**
     * FileException constructor
     *
     * @param message message for the exception
     */
    public FileNotReadableException(String message) {
        super(message);
    }
}
