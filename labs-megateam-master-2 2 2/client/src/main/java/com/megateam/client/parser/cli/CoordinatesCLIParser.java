package com.megateam.client.parser.cli;

import com.megateam.client.resolving.ResolvingMode;
import com.megateam.common.data.Coordinates;
import com.megateam.common.data.validation.CoordinatesValidator;
import com.megateam.common.exception.impl.parsing.UserInterruptedException;
import com.megateam.common.util.Printer;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Scanner;

/** Provides an ability to parse coordinates object from console */
@RequiredArgsConstructor
public class CoordinatesCLIParser {
    /** Printer instance */
    private final Printer printer;

    /** Scanner instance */
    private final Scanner scanner;

    /** Parser resolving mode */
    @Setter private ResolvingMode mode;

    /**
     * Offers an ability to interrupt data input
     *
     * @throws UserInterruptedException if got not y/Y from user
     */
    private void proposeContinue() throws UserInterruptedException {
        printer.print("Do you want to continue? [y/Y - for yes, other - for no]: ");
        String userInput = scanner.nextLine().trim();
        if (!"Y".equalsIgnoreCase(userInput))
            throw new UserInterruptedException("Data input successfully interrupted");
    }

    /**
     * Method is parsing X coordinate
     *
     * @return float variable of X coordinate
     * @throws UserInterruptedException if got not y/Y from user
     */
    private Float parseXCoord() throws UserInterruptedException {

        printer.print("Enter X coordinate (float & greater than -390): ");

        if (!scanner.hasNextLine())
            throw new UserInterruptedException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput)) {
            if (mode == ResolvingMode.CREATE) {
                printer.println(
                        "You're not able to insert a null value for float variable. Try another value.");
                proposeContinue();
                return parseXCoord();
            } else {
                return null;
            }
        }

        try {
            float x = Float.parseFloat(userInput);
            CoordinatesValidator.validateXCoord(x);
            return x;
        } catch (NumberFormatException e) {
            printer.println("X coordinate should be a float.");
            proposeContinue();
            return parseXCoord();
        } catch (Exception e) {
            printer.println(e.getMessage());
            proposeContinue();
            return parseXCoord();
        }
    }

    /**
     * Method is parsing Y coordinate
     *
     * @return float variable of Y coordinate
     * @throws UserInterruptedException if got not y/Y from user
     */
    private Integer parseYCoord() throws UserInterruptedException {
        printer.print("Enter Y coordinate (Integer): ");

        if (!scanner.hasNextLine())
            throw new UserInterruptedException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput)) {
            if (mode == ResolvingMode.CREATE) {
                printer.println("This variable cannot be null. Try another value.");
                proposeContinue();
                return parseYCoord();
            } else {
                return null;
            }
        }

        try {
            Integer y = Integer.parseInt(userInput);
            CoordinatesValidator.validateYCoord(y);
            return y;
        } catch (NumberFormatException e) {
            printer.println("X coordinate should be an Integer.");
            proposeContinue();
            return parseYCoord();
        } catch (Exception e) {
            printer.println(e.getMessage());
            proposeContinue();
            return parseYCoord();
        }
    }

    /**
     * Creates an instance of coordinates and returns it
     *
     * @return instance of coordinates
     * @throws UserInterruptedException if input stream ended or process interrupted by user
     */
    public Coordinates parseCoordinates() throws UserInterruptedException {
        printer.println("#### ENTERING COORDINATES ####");
        Float x = parseXCoord();
        Integer y = parseYCoord();
        printer.println("#### ENTERING COORDINATES ENDED ####");

        return new Coordinates(x, y);
    }
}
