package com.megateam.client.resolving;

import com.megateam.common.CommandFactory;
import com.megateam.client.parser.script.TicketScriptParser;
import com.megateam.common.command.Command;
import com.megateam.common.command.impl.ExecuteScriptCommand;
import com.megateam.common.command.impl.UpdateCommand;
import com.megateam.common.data.Ticket;
import com.megateam.common.data.validation.TicketValidator;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.ParsingException;
import com.megateam.common.exception.ValidationException;
import com.megateam.common.exception.impl.command.CommandExecutionFailedException;
import com.megateam.common.exception.impl.command.CommandResolvingFailedException;
import com.megateam.common.resolving.ResolvingService;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/** Command script resolving service */
@RequiredArgsConstructor
public class CommandScriptResolvingService implements ResolvingService {
    /** Command factory instance */
    private final CommandFactory commandFactory;

    /**
     * Method that executes command script from the specified file
     *
     * @param script command script for resolving
     * @return a list of resolved commands
     * @throws CommandException if something went wrong during command operations
     * @throws ValidationException if something went wrong during validation
     * @throws ParsingException if something went wrong during parsing
     */
    @Override
    public List<Command> resolve(File script)
            throws CommandException, ValidationException, ParsingException {
        List<Command> resolvedCommands = new ArrayList<>();

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(script)))) {
            while (scanner.hasNextLine()) {
                String[] separatedCommandLine = scanner.nextLine().trim().split(" ");
                List<String> args =
                        Arrays.asList(
                                Arrays.copyOfRange(
                                        separatedCommandLine, 1, separatedCommandLine.length));

                Command command = commandFactory.newCommand(separatedCommandLine[0], args);

                if (args.size() != command.getAmountOfArguments()) {
                    throw new CommandExecutionFailedException(
                            "Invalid amount of arguments. Check help for more info");
                }

                if (command.getRequiresElement()) {
                    Ticket ticket;

                    if (!(command instanceof UpdateCommand)) {
                        ticket = TicketScriptParser.parseTicket(scanner, ResolvingMode.CREATE);
                        TicketValidator.validateTicket(ticket);
                    } else {
                        ticket = TicketScriptParser.parseTicket(scanner, ResolvingMode.UPDATE);
                    }
                    command.setAdditionalArgument(ticket);
                }

                if (command instanceof ExecuteScriptCommand) {
                    command.setResolvingService(this);
                }

                resolvedCommands.add(command);
            }
            return resolvedCommands;
        } catch (FileNotFoundException e) {
            throw new CommandResolvingFailedException(
                    "Failed to resolve the script commands: script file does not exist");
        }
    }
}
