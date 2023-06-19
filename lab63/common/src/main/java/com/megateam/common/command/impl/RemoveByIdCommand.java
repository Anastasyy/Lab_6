package com.megateam.common.command.impl;

import com.megateam.common.command.Command;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.exception.ParsingException;
import com.megateam.common.util.Printer;
import com.megateam.common.util.TypesParser;

import java.util.List;

/** Removes element by specified id */
public class RemoveByIdCommand extends Command {
    /**
     * RemoveByIdCommand constructor
     *
     * @param arguments command arguments
     * @param printer command printer instance
     */
    public RemoveByIdCommand(List<String> arguments, Printer printer) {
        super("remove_by_id", arguments, printer, false, 1);
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
        try {
            dao.remove(TypesParser.parseInteger(arguments.get(0)));
            return "Item was removed";
        } catch (ParsingException e) {
            return e.getMessage();

        }
    }
}
