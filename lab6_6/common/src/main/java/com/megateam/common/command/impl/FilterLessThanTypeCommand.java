package com.megateam.common.command.impl;

import com.megateam.common.command.Command;
import com.megateam.common.data.Ticket;
import com.megateam.common.data.util.TicketType;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.exception.ParsingException;
import com.megateam.common.util.Printer;
import com.megateam.common.util.TypesParser;

import java.util.List;
import java.util.stream.Collectors;

/** Prints all elements, which have type attribute less than specified */
public class FilterLessThanTypeCommand extends Command {
    /**
     * FilterLessThanTypeCommand constructor
     *
     * @param arguments command arguments
     * @param printer command printer instance
     */
    public FilterLessThanTypeCommand(List<String> arguments, Printer printer) {
        super("filter_less_than_type", arguments, printer, false, 1);
    }

    /**
     * This method is an abstraction for command execution method
     *
     * @return boolean status of command execution
     * @throws CommandException if something went wrong during the command operations
     * @throws DatabaseException if something went wrong during the database operations
     */
    @Override
    public String  execute() throws CommandException, DatabaseException {
        try {
            TicketType type = TypesParser.parseTicketType(arguments.get(0));

            if (type == null) {
                return "Incorrect element type specified";
            }

            List<Ticket> tickets = dao.findLessThanType(type);

            String ticketsString = tickets.stream()
                    .map(Ticket::toString)
                    .collect(Collectors.joining(", "));

            return ticketsString;
        } catch (ParsingException e) {
            return e.getMessage();
        }
    }
}
