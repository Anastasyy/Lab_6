package com.megateam.common.command.impl;

import com.megateam.common.command.Command;
import com.megateam.common.data.Ticket;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.exception.impl.command.CommandExecutionFailedException;
import com.megateam.common.util.Printer;

import java.util.List;

/** Removes all the elements are lower than specified one */
public class RemoveLowerCommand extends Command {
    /**
     * RemoveLowerCommand constructor
     *
     * @param arguments command arguments
     * @param printer command printer instance
     */
    public RemoveLowerCommand(List<String> arguments, Printer printer) {
        super("remove_lower", arguments, printer, true, 0);
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
                    "Provided element is null. Removing not succeed");
        }

        dao.removeLower(element);
        return  "Successfully removed elements, lower than specified one";
    }
}
