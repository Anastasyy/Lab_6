package com.megateam.client.parser.cli;

import com.megateam.client.resolving.ResolvingMode;
import com.megateam.common.data.Venue;
import com.megateam.common.data.util.VenueType;
import com.megateam.common.data.validation.VenueValidator;
import com.megateam.common.exception.ValidationException;
import com.megateam.common.exception.impl.parsing.UserInterruptedException;
import com.megateam.common.util.Printer;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Scanner;

/** Provides an ability to parse coordinates object from console */
@RequiredArgsConstructor
public class VenueCLIParser {
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
     * Method can parse venue name
     *
     * @return parsed venue name
     * @throws UserInterruptedException if got not y/Y from user
     */
    private String parseVenueName() throws UserInterruptedException {
        printer.print("Enter venue name (non-empty): ");

        if (!scanner.hasNextLine())
            throw new UserInterruptedException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput)) {
            if (mode == ResolvingMode.CREATE) {
                printer.println(
                        "You're not able to insert a null value for this variable. Try another value");
                proposeContinue();
                return parseVenueName();
            } else {
                return null;
            }
        }

        try {
            VenueValidator.validateVenueName(userInput);
            return userInput;
        } catch (ValidationException e) {
            printer.println(e.getMessage());
            proposeContinue();
            return parseVenueName();
        }
    }

    /**
     * Method can parse venue capacity
     *
     * @return venue capacity
     * @throws UserInterruptedException if got y/Y from user
     */
    private Integer parseVenueCapacity() throws UserInterruptedException {
        printer.print("Enter venue capacity (Integer & greater than 0): ");

        if (!scanner.hasNextLine())
            throw new UserInterruptedException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput)) {
            if (mode == ResolvingMode.CREATE) {
                printer.println("This variable cannot be null. Try another value.");
                proposeContinue();
                return parseVenueCapacity();
            } else {
                return null;
            }
        }

        try {
            Integer capacity = Integer.parseInt(userInput);
            VenueValidator.validateVenueCapacity(capacity);
            return capacity;
        } catch (NumberFormatException e) {
            printer.println("Venue capacity should be an Integer.");
            proposeContinue();
            return parseVenueCapacity();
        } catch (ValidationException e) {
            printer.println(e.getMessage());
            proposeContinue();
            return parseVenueCapacity();
        }
    }

    /**
     * Method can parse venue type
     *
     * @return venue type
     * @throws UserInterruptedException if got y/Y from user
     */
    private VenueType parseVenueType() throws UserInterruptedException {
        printer.print(
                String.format(
                        "Enter venue type (can be null) %s: ",
                        Arrays.toString(VenueType.values())));

        if (!scanner.hasNextLine())
            throw new UserInterruptedException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput)) {
            return null;
        }

        try {
            return VenueType.valueOf(userInput);
        } catch (IllegalArgumentException e) {
            printer.println("You should select type from the listed ones.");
            proposeContinue();
            return parseVenueType();
        }
    }

    /**
     * Creates an instance of Venue and return it
     *
     * @return instance of venue
     * @throws UserInterruptedException if input stream ended or process interrupted by user
     */
    public Venue parseVenue() throws UserInterruptedException {
        printer.println("#### ENTERING VENUE ####");
        String name = parseVenueName();
        Integer capacity = parseVenueCapacity();
        VenueType type = parseVenueType();
        printer.println("#### ENTERING VENUE ENDED ####");

        return new Venue(name, capacity, type);
    }
}
