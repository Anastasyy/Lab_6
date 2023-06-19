package com.megateam.server.modules;

import com.megateam.common.ResponseSerializer;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class CommandResponse {
    private ResponseSerializer responseSerializer;

    public CommandResponse(ResponseSerializer responseSerializer) {
        this.responseSerializer = responseSerializer;
    }

    public void sendResponse(String response, DatagramChannel channel, SocketAddress clientAddress) throws Exception {
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
}
