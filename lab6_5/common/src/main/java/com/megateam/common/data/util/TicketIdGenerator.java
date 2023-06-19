package com.megateam.common.data.util;

/** Utility class for generating unique ticket id */
public class TicketIdGenerator {
    /**
     * Ticket id generator
     *
     * @return unique ticket id
     */
    public static int generateNewId() {
        int id = (int) System.currentTimeMillis();
        return (id <= 0) ? (id & 31) : id;
    }
}
