package com.megateam.server;

import com.megateam.common.CommandDeserializer;
import com.megateam.common.CommandFactory;
import com.megateam.common.CommandScriptResolvingService;
import com.megateam.common.ResponseSerializer;
import com.megateam.common.command.Command;
import com.megateam.common.command.impl.ExecuteScriptCommand;
import com.megateam.common.dao.Dao;
import com.megateam.common.data.Ticket;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.exception.impl.database.UnableToLoadDatabaseException;
import com.megateam.common.exception.impl.database.UnableToSaveDatabaseException;
import com.megateam.common.util.ConsolePrinter;
import com.megateam.common.util.FileManipulationService;
import com.megateam.common.util.Printer;
import com.megateam.server.dao.TicketDaoImpl;
import com.megateam.server.database.Database;
import com.megateam.server.database.DatabaseSavingService;
import com.megateam.server.database.TicketDatabaseImpl;
import com.megateam.server.database.data.TicketDatabaseDataclass;
import com.megateam.server.execution.CommandScriptExecutionService;
import com.megateam.server.execution.SingleCommandExecutionService;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class UDPServer {
    private DatagramChannel channel;
    private CommandDeserializer commandDeserializer;
    private ResponseSerializer responseSerializer;
    private SingleCommandExecutionService executor;
    private Dao<Ticket> dao;

    public UDPServer(int port, SingleCommandExecutionService executor, Dao<Ticket> dao) throws IOException {
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        this.executor = executor;
        this.dao = dao;
        this.commandDeserializer = new CommandDeserializer();
        this.responseSerializer = new ResponseSerializer();
    }

    public void listenAndRespond() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteBuffer tempBuffer = ByteBuffer.allocate(1024);
        int bytesRead;

        SocketAddress clientAddress;
        while (true) {
            clientAddress = channel.receive(tempBuffer);
            if (clientAddress != null) {
                bytesRead = tempBuffer.position();
                if (bytesRead > 0) {
                    baos.write(tempBuffer.array(), 0, bytesRead);
                    tempBuffer.clear();
                }

                if (bytesRead < 1024) {
                    break;
                }
            }
        }

        byte[] commandBytes = baos.toByteArray();
        Command command = commandDeserializer.deserialize(commandBytes);
        String result;

        try {
            if (command instanceof ExecuteScriptCommand) {
                if (command instanceof ExecuteScriptCommand) {
                    FileManipulationService fms = new FileManipulationService();
                    ExecuteScriptCommand scriptCommand = (ExecuteScriptCommand) command;
                    scriptCommand.setExecutionService(new CommandScriptExecutionService(dao, fms));
                    Printer printer = new ConsolePrinter();
                    CommandFactory commandFactory = new CommandFactory(printer);
                    scriptCommand.setFms(fms);
                    scriptCommand.setResolvingService(new CommandScriptResolvingService(commandFactory));
                    scriptCommand.setDao(dao);
                    executor.execute(scriptCommand);
                }
            }

            result = executor.execute(command);
        } catch (DatabaseException | CommandException e) {
            result = "Command execution error: " + e.getMessage();
        }

        sendResponse(result, clientAddress);
    }

    public void sendResponse(String response, SocketAddress clientAddress) throws IOException {
        byte[] responseBytes = responseSerializer.serialize(response);
        ByteBuffer buffer = ByteBuffer.wrap(responseBytes);

        int packetSize = 1024; // размер чанка
        while (buffer.hasRemaining()) {
            ByteBuffer chunkBuffer;
            if (buffer.remaining() <= packetSize) {
                chunkBuffer = buffer;
            } else {
                chunkBuffer = ByteBuffer.allocate(packetSize);
                for (int i = 0; i < packetSize; i++) {
                    chunkBuffer.put(buffer.get());
                }
                chunkBuffer.flip();
            }

            channel.send(chunkBuffer, clientAddress);
        }
    }

    public void start() throws IOException, ClassNotFoundException, UnableToSaveDatabaseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (System.in.available() > 0) {
                String line = reader.readLine().strip();
                System.out.println("Command: " + line);
                if ("save".equalsIgnoreCase(line)) {
                    dao.save();
                    System.out.println("Database saved");
                }
            }
            listenAndRespond();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        FileManipulationService fms = new FileManipulationService();
        JAXBContext context = JAXBContext.newInstance(TicketDatabaseDataclass.class);
        Marshaller marshaller = context.createMarshaller();
        Unmarshaller unmarshaller = context.createUnmarshaller();
        DatabaseSavingService dss = new DatabaseSavingService(fms, fms.getFileByName("a"), marshaller, unmarshaller);
        Database<Ticket> database = new TicketDatabaseImpl(dss);

        try {
            database.load();
        } catch (UnableToLoadDatabaseException e) {
            System.out.println(e.getMessage());
            System.out.println("Initializing empty database");
            database.initEmptyDb();
        }

        Dao<Ticket> dao = new TicketDaoImpl(database);
        SingleCommandExecutionService executor = new SingleCommandExecutionService(dao, fms);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                dao.save();
                System.out.println("Database saved at shutdown.");
            } catch (UnableToSaveDatabaseException e) {
                e.printStackTrace();
            }
        }));

        UDPServer server = new UDPServer(8888, executor, dao);
        server.start();
    }
}
