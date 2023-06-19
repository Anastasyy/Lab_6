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

/** A service for single command execution */
@RequiredArgsConstructor
public class SingleCommandExecutionService implements ExecutionService {
    /** Ticket dao instance */
    private final Dao<Ticket> dao;

    /** FileManipulationService instance */
    private final FileManipulationService fms;

    /**
     * This method executes a single command
     *
     * @param command command, that should be executed
     * @return command execution status
     * @throws DefaultExecutorException if something went wrong during command execution
     */
    @Override
    public String execute(Command command) throws CommandException, DatabaseException {
        command.setDao(dao);
        command.setFms(fms);

        if (command instanceof ExecuteScriptCommand) {
            command.setExecutionService(new CommandScriptExecutionService(dao, fms));
        }

        return command.execute();
    }
}
