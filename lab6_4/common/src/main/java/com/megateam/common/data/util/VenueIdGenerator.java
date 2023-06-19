package com.megateam.common.data.util;

/** Utility class for generating unique venue id */
public class VenueIdGenerator {
    /**
     * Venue id generator
     *
     * @return unique venue id
     */
    public static long generateNewId() {
        return System.currentTimeMillis();
    }
}
