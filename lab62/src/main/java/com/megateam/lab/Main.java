package com.megateam.lab;

import com.megateam.common.CommandFactory;
import com.megateam.client.cli.Console;
import com.megateam.common.cli.CoordinatesCLIParser;
import com.megateam.common.cli.TicketCLIParser;
import com.megateam.common.cli.VenueCLIParser;
import com.megateam.client.resolving.SingleCommandResolvingService;
import com.megateam.common.dao.Dao;
import com.megateam.common.data.Ticket;
import com.megateam.common.exception.FileException;
import com.megateam.common.exception.impl.database.UnableToLoadDatabaseException;
import com.megateam.common.execution.ExecutionService;
import com.megateam.common.resolving.ResolvingService;
import com.megateam.common.util.ConsolePrinter;
import com.megateam.common.util.FileManipulationService;
import com.megateam.common.util.Printer;
import com.megateam.server.dao.TicketDaoImpl;
import com.megateam.server.database.Database;
import com.megateam.server.database.DatabaseSavingService;
import com.megateam.server.database.TicketDatabaseImpl;
import com.megateam.server.database.data.TicketDatabaseDataclass;
import com.megateam.server.execution.SingleCommandExecutionService;

import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Main {
    public static void main(String[] args) {
        Printer printer = new ConsolePrinter();
        Scanner scanner = new Scanner(System.in);
        FileManipulationService fms = new FileManipulationService();

        try {
            JAXBContext context = JAXBContext.newInstance(TicketDatabaseDataclass.class);
            Marshaller marshaller = context.createMarshaller();
            Unmarshaller unmarshaller = context.createUnmarshaller();

            DatabaseSavingService dss =
                    new DatabaseSavingService(
                            fms,
                            fms.getFileByName("a"),
                            marshaller,
                            unmarshaller);

            Database<Ticket> database = new TicketDatabaseImpl(dss);

            try {
                database.load();
            } catch (UnableToLoadDatabaseException e) {
                printer.println(e.getMessage());
                printer.println("Initializing empty database");
                database.initEmptyDb();
            }

            Dao<Ticket> dao = new TicketDaoImpl(database);

            CommandFactory commandFactory = new CommandFactory(printer);
            CoordinatesCLIParser coordinatesCLIParser = new CoordinatesCLIParser(printer, scanner);
            VenueCLIParser venueCLIParser = new VenueCLIParser(printer, scanner);
            TicketCLIParser ticketCLIParser =
                    new TicketCLIParser(printer, scanner, coordinatesCLIParser, venueCLIParser);
            ResolvingService resolvingService =
                    new SingleCommandResolvingService(commandFactory, ticketCLIParser);
            ExecutionService executionService = new SingleCommandExecutionService(dao, fms);

            Console console = new Console(scanner, printer, resolvingService, executionService);
            console.run();
        } catch (FileException | JAXBException e) {
            printer.println(e.getMessage());
            printer.println(
                    "Unable to create JAXB context. Saving / loading operations are not available");
        }
    }
}
