package com.megateam.server.modules;

import com.megateam.common.CommandFactory;
import com.megateam.common.CommandScriptResolvingService;
import com.megateam.common.command.Command;
import com.megateam.common.command.impl.ExecuteScriptCommand;
import com.megateam.common.dao.Dao;
import com.megateam.common.data.Ticket;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.util.ConsolePrinter;
import com.megateam.common.util.FileManipulationService;
import com.megateam.common.util.Printer;
import com.megateam.server.execution.CommandScriptExecutionService;
import com.megateam.server.execution.SingleCommandExecutionService;

public class ExecuteCommandListener {
    private SingleCommandExecutionService executor;
    private Dao<Ticket> dao;

    public ExecuteCommandListener(SingleCommandExecutionService executor, Dao<Ticket> dao) {
        this.executor = executor;
        this.dao = dao;
    }

    public String execute(Command command) {
        String result;

        try {
            if (command instanceof ExecuteScriptCommand) {
                if (command instanceof ExecuteScriptCommand) {
                    FileManipulationService fms = new FileManipulationService();
                    ExecuteScriptCommand scriptCommand = (ExecuteScriptCommand) command;
                    scriptCommand.setExecutionService(new CommandScriptExecutionService(dao, fms));
                    Printer printer = new ConsolePrinter();
                    CommandFactory commandFactory = new CommandFactory(printer);
                    scriptCommand.setFms(fms);
                    scriptCommand.setResolvingService(new CommandScriptResolvingService(commandFactory));
                    scriptCommand.setDao(dao);
                    executor.execute(scriptCommand);
                }
            }

            result = executor.execute(command);
            return result;
        } catch (DatabaseException | CommandException e) {
            result = "Command execution error: " + e.getMessage();
            return result;
        }

    }
}
