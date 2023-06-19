package com.megateam.common;

import com.megateam.common.command.Command;
import com.megateam.common.command.impl.*;
import com.megateam.common.exception.impl.command.CommandNotFoundException;
import com.megateam.common.util.Printer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/** This class is a factory for every existing command */
@RequiredArgsConstructor
public class CommandFactory {

    /** Printer instance */
    private final Printer printer;

    /**
     * This factory method returns a resolved command instance
     *
     * @param commandName command name
     * @param args command arguments
     * @return command instance
     * @throws CommandNotFoundException if command not found after resolving
     */
    public Command newCommand(@NonNull String commandName, @NonNull List<String> args)
            throws CommandNotFoundException {
        Command command =
                switch (commandName) {
                    case "help" -> new HelpCommand(args, printer);
                    case "info" -> new InfoCommand(args, printer);
                    case "show" -> new ShowCommand(args, printer);
                    case "add" -> new AddCommand(args, printer);
                    case "update" -> new UpdateCommand(args, printer);
                    case "remove_by_id" -> new RemoveByIdCommand(args, printer);
                    case "clear" -> new ClearCommand(args, printer);
                    case "save" -> new SaveCommand(args, printer);
                    case "execute_script" -> new ExecuteScriptCommand(args, printer);
                    case "remove_first" -> new RemoveFirstCommand(args, printer);
                    case "remove_last" -> new RemoveLastCommand(args, printer);
                    case "remove_lower" -> new RemoveLowerCommand(args, printer);
                   // case "remove_any_by_refundable" -> new RemoveAnyByRefundableCommand(
                           // args, printer);
                    case "filter_less_than_type" -> new FilterLessThanTypeCommand(args, printer);
                    case "print_field_descending_price" -> new PrintFieldDescendingPriceCommand(
                            args, printer);

                    default -> throw new CommandNotFoundException(
                            "Command '" + commandName + "' not found");
                };

        return command;
    }
}
