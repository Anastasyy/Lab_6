package com.megateam.common.command.impl;

import com.megateam.common.command.Command;
import com.megateam.common.data.Ticket;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.util.Printer;

import java.util.List;
import java.util.stream.Collectors;

/** Prints all elements' prices in descending order */
public class PrintFieldDescendingPriceCommand extends Command {
    /**
     * PrintFieldDescendingPriceCommand constructor
     *
     * @param arguments command arguments
     * @param printer command printer instance
     */
    public PrintFieldDescendingPriceCommand(List<String> arguments, Printer printer) {
        super("print_field_descending_price", arguments, printer, false, 0);
    }

    /**
     * This method is an abstraction for command execution method
     *
     * @return boolean status of command execution
     * @throws CommandException if something went wrong during the command operations
     * @throws DatabaseException if something went wrong during the database operations
     */
    @Override
    public String execute() throws CommandException, DatabaseException {
        String prices =
                dao.findAll().stream()
                        .map(Ticket::getPrice)
                        .sorted((el1, el2) -> Float.compare(el2, el1))
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));

        return prices;
    }
}
