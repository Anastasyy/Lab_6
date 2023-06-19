package com.megateam.client.parser.script;

import com.megateam.client.resolving.ResolvingMode;
import com.megateam.common.data.Coordinates;
import com.megateam.common.data.Ticket;
import com.megateam.common.data.Venue;
import com.megateam.common.data.util.TicketType;
import com.megateam.common.data.validation.TicketValidator;
import com.megateam.common.exception.ValidationException;
import com.megateam.common.exception.impl.parsing.DataclassParsingException;
import com.megateam.common.util.TypesParser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;

/** Class for parsing ticket from script */
public class TicketScriptParser {
    /**
     * Parse ticket from script
     *
     * @param scanner script scanner for ticket scanning
     * @param mode ticket resolving mode
     * @return ticket instance
     * @throws DataclassParsingException if something went wrong during parsing dataclass from
     *     script
     */
    public static Ticket parseTicket(Scanner scanner, ResolvingMode mode)
            throws DataclassParsingException {
        try {
            String name = TypesParser.parseString(scanner.nextLine());
            Coordinates coordinates = CoordinatesScriptParser.parseCoordinates(scanner, mode);
            Float price = TypesParser.parseFloat(scanner.nextLine());
            String comment = TypesParser.parseString(scanner.nextLine());
            Boolean refundable = TypesParser.parseBoolean(scanner.nextLine());
            TicketType ticketType = TypesParser.parseTicketType(scanner.nextLine());
            Venue venue = VenueScriptParser.parseVenue(scanner, mode);

            Ticket ticket =
                    new Ticket(
                            name,
                            coordinates,
                            LocalDateTime.now(ZoneId.systemDefault()),
                            price,
                            comment,
                            refundable,
                            ticketType,
                            venue);

            if (mode == ResolvingMode.CREATE) {
                TicketValidator.validateTicket(ticket);
            }

            return ticket;
        } catch (ValidationException e) {
            throw new DataclassParsingException(e.getMessage());
        } catch (Exception e) {
            throw new DataclassParsingException("Something went wrong during ticket parsing");
        }
    }
}
