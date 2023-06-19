package com.megateam.server.execution;

import com.megateam.common.command.Command;
import com.megateam.common.command.impl.ExecuteScriptCommand;
import com.megateam.common.dao.Dao;
import com.megateam.common.data.Ticket;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.exception.impl.command.DefaultExecutorException;
import com.megateam.common.execution.ExecutionService;
import com.megateam.common.util.FileManipulationService;

import lombok.RequiredArgsConstructor;

import java.util.List;

/** A service for command script execution */
@RequiredArgsConstructor
public class CommandScriptExecutionService implements ExecutionService {
    /** Ticket dao instance */
    private final Dao<Ticket> dao;

    /** FileManipulationService instance */
    private final FileManipulationService fms;

    /**
     * This method executes a command script
     *
     * @param script command script, that should be executed
     * @return command script execution status
     * @throws DefaultExecutorException if something went wrong during command script execution
     */
    @Override
    public String execute(List<Command> script) throws CommandException, DatabaseException {
        StringBuilder result = new StringBuilder();

        for (Command command : script) {
            command.setDao(dao);
            command.setFms(fms);

            if (command instanceof ExecuteScriptCommand) {
                command.setExecutionService(this);
            }

            // Assuming `command.execute()` returns a string
            String commandResult = command.execute();
            result.append(commandResult).append("\n"); // Append command result and a newline character
        }

        return result.toString();
    }
}
