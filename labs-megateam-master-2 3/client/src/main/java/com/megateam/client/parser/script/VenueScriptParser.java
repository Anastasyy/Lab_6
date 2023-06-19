package com.megateam.client.parser.script;

import com.megateam.client.resolving.ResolvingMode;
import com.megateam.common.data.Venue;
import com.megateam.common.data.util.VenueType;
import com.megateam.common.data.validation.VenueValidator;
import com.megateam.common.exception.ValidationException;
import com.megateam.common.exception.impl.parsing.DataclassParsingException;
import com.megateam.common.util.TypesParser;

import java.util.Scanner;

/** Class for parsing venue from script */
public class VenueScriptParser {
    /**
     * Parse venue from strings
     *
     * @param scanner script scanner
     * @param mode venue resolving mode
     * @return venue instance
     * @throws DataclassParsingException if something went wrong during parsing dataclass from
     *     script
     */
    public static Venue parseVenue(Scanner scanner, ResolvingMode mode)
            throws DataclassParsingException {
        try {
            String name = TypesParser.parseString(scanner.nextLine());
            Integer capacity = TypesParser.parseInteger(scanner.nextLine());
            VenueType type = TypesParser.parseVenueType(scanner.nextLine());

            Venue venue = new Venue(name, capacity, type);

            if (mode == ResolvingMode.CREATE) {
                VenueValidator.validateVenue(venue);
            }

            return venue;
        } catch (ValidationException e) {
            throw new DataclassParsingException(e.getMessage());
        } catch (Exception e) {
            throw new DataclassParsingException("Something went wrong during venue parsing");
        }
    }
}
