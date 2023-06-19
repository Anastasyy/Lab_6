package com.megateam.server;

import com.megateam.common.CommandDeserializer;
import com.megateam.common.ResponseSerializer;
import com.megateam.common.command.Command;
import com.megateam.common.dao.Dao;
import com.megateam.common.data.Ticket;
import com.megateam.common.exception.impl.database.UnableToLoadDatabaseException;
import com.megateam.common.exception.impl.database.UnableToSaveDatabaseException;
import com.megateam.common.util.FileManipulationService;
import com.megateam.server.dao.TicketDaoImpl;
import com.megateam.server.database.Database;
import com.megateam.server.database.DatabaseSavingService;
import com.megateam.server.database.TicketDatabaseImpl;
import com.megateam.server.database.data.TicketDatabaseDataclass;
import com.megateam.server.execution.SingleCommandExecutionService;
import com.megateam.server.modules.CommandResponse;
import com.megateam.server.modules.ConnectionListener;
import com.megateam.server.modules.ExecuteCommandListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Server {
    private DatagramChannel channel;
    private CommandDeserializer commandDeserializer;
    private ResponseSerializer responseSerializer;
    private SingleCommandExecutionService executor;
    private Dao<Ticket> dao;
    private ConnectionListener connectionListener;
    private ExecuteCommandListener executeCommandListener;
    private CommandResponse commandResponse;

    public Server(int port, SingleCommandExecutionService executor, Dao<Ticket> dao) throws IOException {
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        this.executor = executor;
        this.dao = dao;
        this.commandDeserializer = new CommandDeserializer();
        this.responseSerializer = new ResponseSerializer();
        this.connectionListener = new ConnectionListener(this.commandDeserializer, channel);
        this.executeCommandListener = new ExecuteCommandListener(this.executor, this.dao);
        this.commandResponse = new CommandResponse(this.responseSerializer);
    }

    public void listenAndRespond() throws Exception {
        Command command = connectionListener.listen();
        String result = executeCommandListener.execute(command);
        this.commandResponse.sendResponse(result, channel, connectionListener.clientAddress);
    }

    public void start() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Thread consoleThread = new Thread(() -> {
            try {
                while (true) {
                    if (reader.ready()) {
                        String line = reader.readLine().strip();
                        if ("save".equalsIgnoreCase(line)) {
                            dao.save();
                            System.out.println("Database saved");
                        }
                        else if ("exit".equalsIgnoreCase(line)) {
                            System.out.println("Server stopped");
                            dao.save();
                            System.exit(0);
                        } else {
                            System.out.println("Command not found");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnableToSaveDatabaseException e) {
                throw new RuntimeException(e);
            }
        });
        consoleThread.start();

        boolean serverRunning = true;
        while (serverRunning) {
            listenAndRespond();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (!consoleThread.isAlive()) {
                serverRunning = false;
                channel.close(); // Close the connection
            }
        }

        channel.close();
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

        Server server = new Server(8888, executor, dao);
        server.start();
    }
}
