package com.megateam.client;
import com.megateam.common.CommandFactory;
import com.megateam.common.command.Command;
import com.megateam.common.exception.impl.command.CommandNotFoundException;
import com.megateam.common.util.ConsolePrinter;
import com.megateam.common.util.Printer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/** Main class for the client side */
public class Client {
    /**
     * Client entry point
     *
     * @param args command line arguments for server jar
     */
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 5555;
        Printer printer = new ConsolePrinter();

        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Connected to the server");

            Scanner userInputScanner = new Scanner(System.in);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            CommandFactory commandFactory = new CommandFactory(printer);

            String commandLine;
            String response;
            do {
                System.out.print("Enter a command: ");
                commandLine = userInputScanner.nextLine();
                List<String> commandTokens = Arrays.asList(commandLine.split(" "));
                String commandName = commandTokens.get(0);
                List<String> commandArgs = new ArrayList<>(commandTokens.subList(1, commandTokens.size()));

                try {
                    Command command = commandFactory.newCommand(commandName, commandArgs);
                    objectOutputStream.writeObject(command);
                    objectOutputStream.flush();
                    response = (String) objectInputStream.readObject();
                    System.out.println(response);
                } catch (CommandNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.out.println("Client exception: " + e.getMessage());
                    e.printStackTrace();
                }

            } while (!commandLine.equalsIgnoreCase("exit"));

        } catch (IOException e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
