package ru.makhach.zerobitcounter.client.reader;

import ru.makhach.zerobitcounter.client.handler.ByteProcessorHandler;
import ru.makhach.zerobitcounter.client.output.MessageSender;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Приём байтов от сервера
 */
public class SocketReader implements Runnable {
    private final Socket socket;
    private final MessageSender messageSender;
    private final ByteProcessorHandler processorHandler;

    public SocketReader(Socket socket, ByteProcessorHandler processorHandler) {
        this.socket = socket;
        this.processorHandler = processorHandler;
        messageSender = new MessageSender();
    }

    @Override
    public void run() {
        int sum = 0;
        try {
            InputStream is = socket.getInputStream();
            int readByte;
            while ((readByte = is.read()) != -1) {
                System.out.println("READ " + readByte);
                sum += processorHandler.handleByte((byte) readByte);
            }
            messageSender.send(socket, sum);
            System.out.println("SUM " + sum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
