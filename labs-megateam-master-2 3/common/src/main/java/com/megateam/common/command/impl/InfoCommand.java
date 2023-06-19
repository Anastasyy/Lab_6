package com.megateam.common.command.impl;

import com.megateam.common.command.Command;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.util.Printer;

import java.time.format.DateTimeFormatter;
import java.util.List;

/** Command prints collection information */
public class InfoCommand extends Command {
    /**
     * InfoCommand constructor
     *
     * @param arguments command arguments
     * @param printer command printer
     */
    public InfoCommand(List<String> arguments, Printer printer) {
        super("info", arguments, printer, false, 0);
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
        StringBuilder sb = new StringBuilder();
        sb.append("Collection info:").append('\n');
        sb.append("Type: ").append("ArrayList").append('\n');
        String creationDate =
                dao.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        sb.append("Creation date: ").append(creationDate).append('\n');
        sb.append("Amount of elements: ").append(dao.size());
        return sb.toString();
    }
}
