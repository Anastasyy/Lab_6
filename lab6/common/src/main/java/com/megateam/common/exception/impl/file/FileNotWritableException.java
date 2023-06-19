package com.megateam.common.exception.impl.file;

import com.megateam.common.exception.FileException;

/** This exception is thrown if some file is not writable */
public class FileNotWritableException extends FileException {
    /**
     * FileException constructor
     *
     * @param message message for the exception
     */
    public FileNotWritableException(String message) {
        super(message);
    }
}
