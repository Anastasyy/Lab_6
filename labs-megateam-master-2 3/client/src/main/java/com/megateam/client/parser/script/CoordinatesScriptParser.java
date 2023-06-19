package com.megateam.client.parser.script;

import com.megateam.client.resolving.ResolvingMode;
import com.megateam.common.data.Coordinates;
import com.megateam.common.data.validation.CoordinatesValidator;
import com.megateam.common.exception.ValidationException;
import com.megateam.common.exception.impl.parsing.DataclassParsingException;
import com.megateam.common.util.TypesParser;

import java.util.Scanner;

/** Class for parsing coordinates from script */
public class CoordinatesScriptParser {
    /**
     * Parse coordinates from strings
     *
     * @param scanner scanner instance
     * @param mode coordinates resolving mode
     * @return coordinates instance
     * @throws DataclassParsingException if something went wrong during parsing dataclass from
     *     script
     */
    public static Coordinates parseCoordinates(Scanner scanner, ResolvingMode mode)
            throws DataclassParsingException {
        try {
            Float x = TypesParser.parseFloat(scanner.nextLine());
            Integer y = TypesParser.parseInteger(scanner.nextLine());

            Coordinates coordinates = new Coordinates(x, y);

            if (mode == ResolvingMode.CREATE) {
                CoordinatesValidator.validateCoordinates(coordinates);
            }

            return coordinates;
        } catch (ValidationException e) {
            throw new DataclassParsingException(e.getMessage());
        } catch (Exception e) {
            throw new DataclassParsingException("Something went wrong during coordinates parsing");
        }
    }
}
