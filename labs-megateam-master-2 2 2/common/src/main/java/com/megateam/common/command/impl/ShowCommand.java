package com.megateam.common.command.impl;

import com.megateam.common.command.Command;
import com.megateam.common.data.Ticket;
import com.megateam.common.exception.CommandException;
import com.megateam.common.util.Printer;

import java.util.List;

/** Command shows a list of stored elements */
public class ShowCommand extends Command {
    /**
     * ShowCommand constructor
     *
     * @param arguments command arguments
     * @param printer command printer
     */
    public ShowCommand(List<String> arguments, Printer printer) {
        super("show", arguments, printer, false, 0);
    }

    /**
     * This method is an abstraction for command execution method
     *
     * @return boolean status of command execution
     */
    @Override
    public String execute() throws CommandException {
        List<Ticket> tickets = dao.findAll();
        StringBuilder sb = new StringBuilder();
        for (Ticket item : tickets) {
            sb.append(item.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
