package com.megateam.client.resolving;

import com.megateam.common.CommandFactory;
import com.megateam.client.parser.cli.TicketCLIParser;
import com.megateam.common.command.Command;
import com.megateam.common.command.impl.ExecuteScriptCommand;
import com.megateam.common.command.impl.UpdateCommand;
import com.megateam.common.data.Ticket;
import com.megateam.common.data.validation.TicketValidator;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.ParsingException;
import com.megateam.common.exception.ValidationException;
import com.megateam.common.exception.impl.command.CommandExecutionFailedException;
import com.megateam.common.resolving.ResolvingService;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

/** Single command resolving service */
@RequiredArgsConstructor
public class SingleCommandResolvingService implements ResolvingService {
    /** Command factory instance */
    private final CommandFactory commandFactory;

    /** TicketCLIParser instance */
    private final TicketCLIParser ticketCLIParser;

    /**
     * Single command resolving method
     *
     * @param commandLine command line with arguments
     * @return command instance
     * @throws CommandException if something went wrong during command operations
     * @throws ValidationException if something went wrong during validation operations
     * @throws ParsingException if something went wrong during parsing operations
     */
    @Override
    public Command resolve(String commandLine)
            throws CommandException, ValidationException, ParsingException {
        String[] separatedLine = commandLine.trim().split(" ");
        List<String> args =
                Arrays.asList(Arrays.copyOfRange(separatedLine, 1, separatedLine.length));

        Command command = commandFactory.newCommand(separatedLine[0], args);

        if (args.size() != command.getAmountOfArguments()) {
            throw new CommandExecutionFailedException(
                    "Invalid amount of arguments. Check help for more info");
        }

        if (command.getRequiresElement()) {
            Ticket ticket;

            if (!(command instanceof UpdateCommand)) {
                ticketCLIParser.setMode(ResolvingMode.CREATE);
                ticket = ticketCLIParser.parseTicket();
                TicketValidator.validateTicket(ticket);
            } else {
                ticketCLIParser.setMode(ResolvingMode.UPDATE);
                ticket = ticketCLIParser.parseTicket();
            }

            command.setAdditionalArgument(ticket);
        }

        if (command instanceof ExecuteScriptCommand) {
            command.setResolvingService(new CommandScriptResolvingService(commandFactory));
        }

        return command;
    }
}
