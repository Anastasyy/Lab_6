package com.megateam.common;

import com.megateam.common.command.Command;

import java.io.*;

public class CommandSerializer {
    public byte[] serialize(Command command) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(command);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
