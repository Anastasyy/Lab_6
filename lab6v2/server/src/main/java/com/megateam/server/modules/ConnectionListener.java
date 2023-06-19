package com.megateam.server.modules;

import com.megateam.common.CommandDeserializer;
import com.megateam.common.command.Command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ConnectionListener {
    private CommandDeserializer commandDeserializer;
    private DatagramChannel channel;
    public SocketAddress clientAddress;

    public ConnectionListener(CommandDeserializer commandDeserializer, DatagramChannel channel) {
        this.commandDeserializer = commandDeserializer;
        this.channel = channel;
    }


    public Command listen() throws IOException {
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
        this.clientAddress = clientAddress;
        return command;
    }
}
