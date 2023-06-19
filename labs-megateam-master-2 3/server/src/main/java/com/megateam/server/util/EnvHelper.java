package com.megateam.server.util;

/** Utility class for env operations */
public class EnvHelper {
    /**
     * Method for retrieving db saving file from the env variable SAVING_FILE
     *
     * @return db saving file location
     */
    public static String retrieveSavingFileLocation() {
        String location = System.getenv("SAVING_FILE");
        return (location == null) ? "" : location;
    }
}
