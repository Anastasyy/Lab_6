package com.megateam.common.command;

import com.megateam.common.dao.Dao;
import com.megateam.common.data.Ticket;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.execution.ExecutionService;
import com.megateam.common.resolving.ResolvingService;
import com.megateam.common.util.FileManipulationService;
import com.megateam.common.util.Printer;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/** Abstraction for all commands */
@RequiredArgsConstructor
public abstract class Command implements Serializable {
    @NonNull @Getter private final String name;
    /** List of command arguments */
    protected final List<String> arguments;

    /** Command printer instance */
    @NonNull @Getter @Setter protected transient Printer printer;


    /** Command requires collection element status */
    @NonNull @Getter protected Boolean RequiresElement;

    /** Amount of command arguments */
    @Getter protected final int amountOfArguments;

    /** Ticket element is stored there if the command requires it */
    @Setter protected Object additionalArgument;

    /** Dao for command */
    @Setter protected Dao<Ticket> dao;

    /** File manipulation service */
    @Setter protected FileManipulationService fms;

    /** Command execution service (mainly user for script execution) */
    @Setter protected ExecutionService executionService;

    /** Resolving service (mainly user for script commands resolving) */
    @Setter protected ResolvingService resolvingService;



    /**
     * This method is an abstraction for command execution method
     *
     * @return boolean status of command execution
     * @throws CommandException if something went wrong during the command operations
     * @throws DatabaseException if something went wrong during the database operations
     */
    public abstract String execute() throws CommandException, DatabaseException;
}
