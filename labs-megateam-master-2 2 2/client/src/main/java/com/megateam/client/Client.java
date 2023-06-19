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

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 5555;
        byte[] buffer = new byte[65535];

        Printer printer = new ConsolePrinter();
        CommandFactory commandFactory = new CommandFactory(printer);

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(hostname);

            Scanner userInputScanner = new Scanner(System.in);

            String commandLine;
            do {
                System.out.print("Enter a command: ");
                commandLine = userInputScanner.nextLine();
                List<String> commandTokens = Arrays.asList(commandLine.split(" "));
                String commandName = commandTokens.get(0);
                List<String> commandArgs = new ArrayList<>(commandTokens.subList(1, commandTokens.size()));

                try {
                    Command command = commandFactory.newCommand(commandName, commandArgs);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(command);
                    byte[] commandBytes = baos.toByteArray();

                    DatagramPacket commandPacket = new DatagramPacket(commandBytes, commandBytes.length, serverAddress, port);
                    socket.send(commandPacket);

                    DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(responsePacket);

                    ByteArrayInputStream bais = new ByteArrayInputStream(responsePacket.getData(), 0, responsePacket.getLength());
                    ObjectInputStream ois = new ObjectInputStream(bais);

                    String response = (String) ois.readObject();
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
