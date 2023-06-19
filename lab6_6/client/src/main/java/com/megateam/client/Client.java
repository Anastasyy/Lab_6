package com.megateam.client;

import com.megateam.common.cli.CoordinatesCLIParser;
import com.megateam.common.cli.TicketCLIParser;
import com.megateam.common.cli.VenueCLIParser;
import com.megateam.client.resolving.SingleCommandResolvingService;
import com.megateam.common.CommandFactory;
import com.megateam.common.CommandSerializer;
import com.megateam.common.ResponseDeserializer;
import com.megateam.common.command.Command;
import com.megateam.common.exception.CommandException;
import com.megateam.common.exception.ParsingException;
import com.megateam.common.exception.ValidationException;
import com.megateam.common.exception.impl.parsing.UserInterruptedException;
import com.megateam.common.util.ConsolePrinter;
import com.megateam.common.util.Printer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Scanner;

public class Client {
    private DatagramChannel channel;
    private InetSocketAddress serverAddress;
    private SingleCommandResolvingService resolver;
    private CommandSerializer commandSerializer;
    private ResponseDeserializer responseDeserializer;

    public Client(String host, int port, SingleCommandResolvingService resolver) throws IOException {
        serverAddress = new InetSocketAddress(host, port);
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.connect(serverAddress);
        this.resolver = resolver;
        this.commandSerializer = new CommandSerializer();
        this.responseDeserializer = new ResponseDeserializer();
    }

    public void sendCommand(Command command) throws IOException {
        byte[] commandBytes = commandSerializer.serialize(command);
        ByteBuffer buffer = ByteBuffer.wrap(commandBytes);

        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_WRITE);

        int packetSize = 1024; // размер чанка
        int timeoutMillis = 2000; // задержка в миллисекундах
        while (buffer.hasRemaining()) {
            if (selector.select(timeoutMillis) == 0) {
                // таймаут истек, бросаем исключение
                throw new SocketTimeoutException("Timeout while sending command");
            }

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

            channel.send(chunkBuffer, serverAddress);
            selector.selectedKeys().clear();
        }

        selector.close();
    }

    public String receiveResponse() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead;

        while (true) {
            bytesRead = channel.read(buffer);
            if (bytesRead > 0) {
                baos.write(buffer.array(), 0, bytesRead);
                buffer.clear();
            }

            if (bytesRead < 1024) {
                break;
            }
        }

        byte[] fullData = baos.toByteArray();
        String response = responseDeserializer.deserialize(fullData);

        return response;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Enter command line:");
                String commandLine = scanner.nextLine();

                if (commandLine.equalsIgnoreCase("exit")) {
                    break;
                }

                try {
                    Command command = resolver.resolve(commandLine);
                    sendCommand(command);
                    try {
                        Thread.sleep(1000);  // wait for 1 second
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    String response = receiveResponse();
                    System.out.println("Received response: " + response);
                }
                catch (UserInterruptedException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                catch (SocketTimeoutException e) {
                    System.out.println("Timeout while sending command. Please try again.");
                }
                catch (java.net.PortUnreachableException e) {
                    System.out.println("Could not reach the server port. Please check your server and try again.");
                }
                catch (CommandException e) {
                    System.out.println("Command error: " + e.getMessage());
                } catch (ValidationException | ParsingException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        Printer printer = new ConsolePrinter();
        CommandFactory commandFactory = new CommandFactory(printer);
        Scanner scanner = new Scanner(System.in);
        CoordinatesCLIParser coordinatesCLIParser = new CoordinatesCLIParser(printer, scanner);
        VenueCLIParser venueCLIParser = new VenueCLIParser(printer, scanner);
        TicketCLIParser ticketCLIParser =
                new TicketCLIParser(printer, scanner, coordinatesCLIParser, venueCLIParser);
        SingleCommandResolvingService resolver = new SingleCommandResolvingService(commandFactory, ticketCLIParser);
        Client client = new Client("localhost", 8888, resolver);
        client.start();
    }
}
