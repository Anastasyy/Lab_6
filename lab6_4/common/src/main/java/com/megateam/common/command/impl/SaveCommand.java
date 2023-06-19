package com.megateam.common.command.impl;

import com.megateam.common.command.Command;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.util.Printer;

import java.util.List;

/** Saves existing db into a saving file */
public class SaveCommand extends Command {
    /**
     * SaveCommand constructor
     *
     * @param arguments command arguments
     * @param printer command printer instance
     */
    public SaveCommand(List<String> arguments, Printer printer) {
        super("save", arguments, printer, false, 0);
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
        dao.save();
        return "Database saving succeed";
    }
}
