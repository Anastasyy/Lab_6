package com.megateam.common.execution;

import com.megateam.common.command.Command;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.exception.impl.command.DefaultExecutorException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Execution services abstraction */
public interface ExecutionService {
    /** Executed scripts */
    Set<String> EXECUTED_SCRIPTS = new HashSet<>();

    /**
     * This method is an abstraction for single command execution method
     *
     * @param command command, that should be executed
     * @return command execution status
     * @throws CommandException if something went wrong during command execution
     * @throws DatabaseException if something went wrong during database operations
     * @throws DefaultExecutorException if something went wrong during command execution
     */
    default String execute(Command command) throws CommandException, DatabaseException {
        throw new DefaultExecutorException(
                "You are now able to use default command execution method");
    }

    /**
     * This method is an abstraction for commands script execution method
     *
     * @param script script to be executed
     * @return script execution status
     * @throws CommandException if something went wrong during command execution
     * @throws DatabaseException if something went wrong during database operations
     * @throws DefaultExecutorException if something went wrong during command script execution
     */
    default String execute(List<Command> script) throws CommandException, DatabaseException {
        throw new DefaultExecutorException(
                "You are now able to use default script execution method");
    }
}
