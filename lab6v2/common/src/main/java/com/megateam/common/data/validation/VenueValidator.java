package com.megateam.common.data.validation;

import com.megateam.common.data.Venue;
import com.megateam.common.exception.impl.validation.BoundValidationException;
import com.megateam.common.exception.impl.validation.CannotBeEmptyException;
import com.megateam.common.exception.impl.validation.NullValidationException;

/** Utility class for venue validation */
public class VenueValidator {
    /**
     * Method validates venue id
     *
     * @param id venue id under validation
     * @throws BoundValidationException if venue id is less than or equals to zero
     */
    public static void validateVenueId(long id) throws BoundValidationException {
        if (id <= 0) throw new BoundValidationException("Venue id should be greater than zero");
    }

    /**
     * Validates a venue name for being empty
     *
     * @param name venue name
     * @throws CannotBeEmptyException if venue name is empty
     * @throws NullValidationException if venue name is not specified
     */
    public static void validateVenueName(String name)
            throws NullValidationException, CannotBeEmptyException {
        if (name == null) throw new NullValidationException("Venue name should be specified");

        if ("".equals(name)) throw new CannotBeEmptyException("Venue name cannot be empty");
    }

    /**
     * Validates a venue capacity for being non-null and greater than zero
     *
     * @param capacity venue capacity
     * @throws NullValidationException if venue capacity is null
     * @throws BoundValidationException if venue capacity is less than or equals to zero
     */
    public static void validateVenueCapacity(Integer capacity)
            throws NullValidationException, BoundValidationException {
        if (capacity == null) throw new NullValidationException("Venue capacity cannot be null");

        if (capacity <= 0)
            throw new BoundValidationException("Venue capacity should be greater than zero");
    }

    /**
     * Validates an existing venue instance
     *
     * @param venue venue instance
     * @throws BoundValidationException if some fields are out of bound
     * @throws NullValidationException if some non-null fields are null
     * @throws CannotBeEmptyException if some non-empty fields are empty
     */
    public static void validateVenue(Venue venue)
            throws BoundValidationException, NullValidationException, CannotBeEmptyException {
        if (venue == null) throw new NullValidationException("Venue cannot be null");

        validateVenueName(venue.getName());
        validateVenueCapacity(venue.getCapacity());
    }
}
