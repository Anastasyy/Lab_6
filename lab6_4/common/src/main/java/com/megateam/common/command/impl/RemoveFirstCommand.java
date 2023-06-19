package com.megateam.common.command.impl;

import com.megateam.common.command.Command;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.util.Printer;

import java.util.List;

/** Command removes first element from the db */
public class RemoveFirstCommand extends Command {
    /**
     * RemoveFirstCommand constructor
     *
     * @param arguments command arguments
     * @param printer command printer instance
     */
    public RemoveFirstCommand(List<String> arguments, Printer printer) {
        super("remove_first", arguments, printer, false, 0);
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
        dao.removeFirst();
        return "Successfully removed first element";
    }
}
