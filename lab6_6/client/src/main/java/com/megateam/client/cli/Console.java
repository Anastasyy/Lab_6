package com.megateam.client.cli;

import com.megateam.common.command.Command;
import com.megateam.common.execution.ExecutionService;
import com.megateam.common.resolving.ResolvingService;
import com.megateam.common.util.Printer;

import lombok.RequiredArgsConstructor;

import java.util.Scanner;

/** Main application interface */
@RequiredArgsConstructor
public class Console {
    /** Scanner instance */
    private final Scanner scanner;

    /** Printer instance */
    private final Printer printer;

    /** ResolvingService instance */
    private final ResolvingService resolvingService;

    /** ExecutionService instance */
    private final ExecutionService executionService;

    /** Application initialisation method */
    public void run() {

        while (true) {
            printer.print("Enter command: ");

            if (!scanner.hasNextLine()) {
                printer.println("Successfully interrupted...");
                System.exit(0);
            }

            String line = scanner.nextLine();

            try {
                Command command = resolvingService.resolve(line);
                executionService.execute(command);
            } catch (Exception e) {
                printer.println(e.getMessage());
            }
        }
    }
}
