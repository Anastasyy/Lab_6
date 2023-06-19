package com.megateam.server;

import com.megateam.common.CommandFactory;
import com.megateam.common.command.Command;
import com.megateam.common.dao.Dao;
import com.megateam.common.data.Ticket;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.exception.FileException;
import com.megateam.common.exception.impl.database.UnableToLoadDatabaseException;
import com.megateam.common.execution.ExecutionService;
import com.megateam.common.util.ConsolePrinter;
import com.megateam.common.util.FileManipulationService;
import com.megateam.common.util.Printer;
import com.megateam.server.dao.TicketDaoImpl;
import com.megateam.server.database.Database;
import com.megateam.server.database.DatabaseSavingService;
import com.megateam.server.database.TicketDatabaseImpl;
import com.megateam.server.database.data.TicketDatabaseDataclass;
import com.megateam.server.execution.SingleCommandExecutionService;
import com.megateam.server.util.EnvHelper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.*;

/** Main class for the server side */
public class Server {
    /**
     * Server entry point
     *
     * @param args command line arguments for server jar
     */
    public static void main(String[] args) {
        int port = 5555;
        byte[] buffer = new byte[65535];

        Printer printer = new ConsolePrinter();
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
            ExecutionService executionService = new SingleCommandExecutionService(dao, fms);

            try (DatagramSocket serverSocket = new DatagramSocket(port)) {
                System.out.println("Server is listening on port " + port);

                while (true) {
                    DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
                    serverSocket.receive(incomingPacket);
                    System.out.println("New packet received");

                    ByteArrayInputStream byteStream = new ByteArrayInputStream(incomingPacket.getData(), 0, incomingPacket.getLength());
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);

                    Command command;
                    try {
                        command = (Command) objectInputStream.readObject();
                        System.out.println("Received command: " + command.getName());

                        String result = executionService.execute(command);
                        System.out.println("Command execution result: " + result);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(result);
                        byte[] responseBytes = baos.toByteArray();

                        DatagramPacket responsePacket = new DatagramPacket(
                                responseBytes,
                                responseBytes.length,
                                incomingPacket.getAddress(),
                                incomingPacket.getPort()
                        );
                        serverSocket.send(responsePacket);

                        printer.println("Result of command " + command.getName() + " sent to client");
                    } catch (EOFException e) {
                        // Exit the loop if the client has disconnected
                        break;
                    } catch (ClassNotFoundException | IllegalArgumentException e) {
                        System.out.println("Error reading command: " + e.getMessage());
                        e.printStackTrace();
                        continue;
                    } catch (CommandException | DatabaseException e) {
                        System.out.println("Error executing command: " + e.getMessage());

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject("Error: " + e.getMessage());
                        byte[] responseBytes = baos.toByteArray();

                        DatagramPacket responsePacket = new DatagramPacket(
                                responseBytes,
                                responseBytes.length,
                                incomingPacket.getAddress(),
                                incomingPacket.getPort()
                        );
                        serverSocket.send(responsePacket);
                    }
                }
            } catch (IOException e) {
                System.out.println("Server exception: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (FileException | JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
