package com.megateam.common.data.validation;

import com.megateam.common.data.Ticket;
import com.megateam.common.exception.impl.validation.BoundValidationException;
import com.megateam.common.exception.impl.validation.CannotBeEmptyException;
import com.megateam.common.exception.impl.validation.NullValidationException;

import java.time.LocalDateTime;

/** Utility class for ticket validation */
public class TicketValidator {
    /**
     * Validates ticket id
     *
     * @param id ticket id
     * @throws NullValidationException if ticket id is null
     * @throws BoundValidationException if ticket id is less than or equals to zero
     */
    public static void validateTicketId(Integer id)
            throws NullValidationException, BoundValidationException {
        if (id == null) throw new NullValidationException("Ticket id cannot be null");

        if (id <= 0) throw new BoundValidationException("Ticket id should be greater than zero");
    }

    /**
     * Validates ticket name for being non-empty and non-null
     *
     * @param name ticket name
     * @throws NullValidationException if ticket name is null
     * @throws CannotBeEmptyException if ticket name is empty
     */
    public static void validateTicketName(String name)
            throws NullValidationException, CannotBeEmptyException {
        if (name == null) throw new NullValidationException("Ticket name should be specified");

        if ("".equals(name)) throw new CannotBeEmptyException("Ticket name cannot be empty");
    }

    /**
     * Validates a ticket creation date for being non-null
     *
     * @param creationDate ticket creation date
     * @throws NullValidationException if ticket creation date is null
     */
    public static void validateTicketCreationDate(LocalDateTime creationDate)
            throws NullValidationException {
        if (creationDate == null)
            throw new NullValidationException("Ticket creation date should be specified");
    }

    /**
     * Validates a ticket price being greater than zero
     *
     * @param price ticket price
     * @throws BoundValidationException if ticket price is less than or equal to zero
     * @throws NullValidationException if ticket price is null
     */
    public static void validateTicketPrice(Float price)
            throws BoundValidationException, NullValidationException {
        if (price == null) throw new NullValidationException("Ticket price cannot be null");

        if (Float.compare(price, 0) <= 0)
            throw new BoundValidationException("Ticket price should be greater than zero");
    }

    /**
     * Validates ticket comment exists and ticket is not longer than 404 symbols
     *
     * @param comment ticket comment
     * @throws NullValidationException if ticket comment is null
     * @throws BoundValidationException if ticket length is greater than 404
     */
    public static void validateTicketComment(String comment)
            throws NullValidationException, BoundValidationException {
        if (comment == null)
            throw new NullValidationException("Ticket comment should be specified");

        if (comment.length() > 404)
            throw new BoundValidationException("Ticket comment should be shorter than 405");
    }

    /**
     * Validates ticket refundable status being non-null
     *
     * @param refundable ticket refundable status
     * @throws NullValidationException if ticket refundable status is null
     */
    public static void validateTicketRefundable(Boolean refundable) throws NullValidationException {
        if (refundable == null)
            throw new NullValidationException("Ticket refundable field cannot be null");
    }

    /**
     * Validates ticket instance
     *
     * @param ticket ticket instance
     * @throws BoundValidationException if some fields are out of bound
     * @throws NullValidationException if some non-null fields are null
     * @throws CannotBeEmptyException if some non-empty fields are empty
     */
    public static void validateTicket(Ticket ticket)
            throws BoundValidationException, NullValidationException, CannotBeEmptyException {
        if (ticket == null) throw new NullValidationException("Ticket cannot be null");

        validateTicketName(ticket.getName());
        CoordinatesValidator.validateCoordinates(ticket.getCoordinates());
        validateTicketCreationDate(ticket.getCreationDate());
        validateTicketPrice(ticket.getPrice());
        validateTicketComment(ticket.getComment());
        validateTicketRefundable(ticket.getRefundable());
        VenueValidator.validateVenue(ticket.getVenue());
    }
}
