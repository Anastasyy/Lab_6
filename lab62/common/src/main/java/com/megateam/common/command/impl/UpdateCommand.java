package com.megateam.common.command.impl;

import com.megateam.common.command.Command;
import com.megateam.common.data.Ticket;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.exception.ParsingException;
import com.megateam.common.exception.impl.command.CommandExecutionFailedException;
import com.megateam.common.util.Printer;
import com.megateam.common.util.TypesParser;

import java.util.List;

/** Updates existing element */
public class UpdateCommand extends Command {
    /**
     * Update command constructor
     *
     * @param arguments command arguments
     * @param printer command printer instance
     */
    public UpdateCommand(List<String> arguments, Printer printer) {
        super("update", arguments, printer, true, 1);
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
        Ticket element = (Ticket) additionalArgument;

        if (element == null) {
            throw new CommandExecutionFailedException(
                    "Provided element is null. Update not required");
        }

        try {
            Integer id = TypesParser.parseInteger(arguments.get(0));
            if (id == null)
                throw new CommandExecutionFailedException("Element id cannot be parsed");

            dao.update(id, element);
            return "Successfully updated element";
        } catch (ParsingException e) {
            return e.getMessage();
        }
    }
}
