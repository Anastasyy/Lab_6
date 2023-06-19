package com.megateam.common.resolving;

import com.megateam.common.command.Command;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.ParsingException;
import com.megateam.common.exception.ValidationException;
import com.megateam.common.exception.impl.command.DefaultResolverException;

import java.io.File;
import java.util.List;

/** Resolving services abstraction */
public interface ResolvingService {
    /**
     * This method is an abstraction for single command resolving method
     *
     * @param commandLine command line with arguments
     * @return command instance
     * @throws CommandException if something went wrong during the command resolving
     * @throws ValidationException if something went wrong during validation
     * @throws ParsingException if something went wrong during parsing
     */
    default Command resolve(String commandLine)
            throws CommandException, ValidationException, ParsingException {
        throw new DefaultResolverException(
                "You are now able to use default command resolving method");
    }

    /**
     * This method is an abstraction for command script resolving method
     *
     * @param script command script for resolving
     * @return list of command prepared for execution
     * @throws CommandException if something went wrong during the command resolving
     * @throws ValidationException if something went wrong during validation
     * @throws ParsingException if something went wrong during parsing
     */
    default List<Command> resolve(File script)
            throws CommandException, ValidationException, ParsingException {
        throw new DefaultResolverException(
                "You are now able to use default command script resolving method");
    }
}
