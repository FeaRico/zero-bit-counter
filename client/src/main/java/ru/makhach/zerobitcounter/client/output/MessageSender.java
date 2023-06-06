package ru.makhach.zerobitcounter.client.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Отправка сообщений серверу
 */
public class MessageSender {
    public void send(Socket socket, int sum) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println(sum);
        printWriter.flush();
    }
}
