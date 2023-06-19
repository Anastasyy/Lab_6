package com.megateam.common.command.impl;

import com.megateam.common.command.Command;
import com.megateam.common.exception.*;
import com.megateam.common.exception.impl.command.RecursionDetectedException;
import com.megateam.common.execution.ExecutionService;
import com.megateam.common.util.FileManipulationService;
import com.megateam.common.util.Printer;

import java.io.File;
import java.util.List;

/** Command for script execution */
public class ExecuteScriptCommand extends Command {
    /**
     * Execute script command constructor
     *
     * @param arguments command arguments
     * @param printer command printer instance
     */
    public ExecuteScriptCommand(List<String> arguments, Printer printer) {
        super("execute_script", arguments, printer, false, 1);
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
            FileManipulationService fms = new FileManipulationService();
            File script = fms.getFileByName(arguments.get(0));
            List<Command> resolvedScript = resolvingService.resolve(script);

            try {
                if (ExecutionService.EXECUTED_SCRIPTS.contains(script.getName())) {
                    throw new RecursionDetectedException(
                            script.getName()
                                    + " is already executed. Recursion detected. Aborting script execution");
                }

                ExecutionService.EXECUTED_SCRIPTS.add(script.getName());
                String executionResult = executionService.execute(resolvedScript);
                ExecutionService.EXECUTED_SCRIPTS.remove(script.getName());
                return executionResult;
            } catch (RecursionDetectedException e) {
                ExecutionService.EXECUTED_SCRIPTS.remove(script.getName());
                throw new RecursionDetectedException(e.getMessage());
            }
        } catch (ParsingException | ValidationException | FileException e) {
            return e.getMessage();
        }
    }
}
