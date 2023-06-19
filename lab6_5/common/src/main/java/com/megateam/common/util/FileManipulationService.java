package com.megateam.common.util;

import com.megateam.common.exception.FileException;
import com.megateam.common.exception.impl.file.FileNotExistsException;
import com.megateam.common.exception.impl.file.FileNotReadableException;
import com.megateam.common.exception.impl.file.FileNotWritableException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/** The service for using files, retrieving them by name and other stuff */
public class FileManipulationService {
    /**
     * This method validates file to be writable
     *
     * @param file file for validation
     * @throws FileNotWritableException if file is not writable
     */
    public void validateFileIsWritable(File file) throws FileNotWritableException {
        if (!Files.isWritable(file.toPath()))
            throw new FileNotWritableException("Specified file is not writable");
    }

    /**
     * This method validates file to be writable
     *
     * @param file file for validation
     * @throws FileNotReadableException if file is not readable
     */
    public void validateFileIsReadable(File file) throws FileNotReadableException {
        if (!Files.isReadable(file.toPath()))
            throw new FileNotReadableException("Specified file is not readable");
    }

    /**
     * This method validates file to exist
     *
     * @param file file for validation
     * @throws FileNotExistsException if file does not exist
     */
    public void validateFileExists(File file) throws FileNotExistsException {
        if (!file.exists()) throw new FileNotExistsException("Specified file does not exists");
    }

    /**
     * Method retrieves file for specified path
     *
     * @param filePath requested file path
     * @return file object
     * @throws FileNotWritableException if specified file is not writable
     * @throws FileNotReadableException if specified file is not readable
     * @throws FileNotExistsException if specified file does not exist
     */
    public File getFileByName(String filePath) throws FileException {
        File file = new File(filePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        validateFileExists(file);
        validateFileIsReadable(file);
        validateFileIsWritable(file);

        return file;
    }
}
