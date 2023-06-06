package ru.makhach.zerobitcounter.client;

import ru.makhach.zerobitcounter.client.handler.CountZeroBitsByteProcessorHandler;
import ru.makhach.zerobitcounter.client.reader.SocketReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5555);
            try (PrintWriter printWriter = new PrintWriter(socket.getOutputStream())) {
                if (args.length == 2 && "--path".equals(args[0])) {
                    printWriter.println(args[1]);
                    System.out.println(args[1]);
                    printWriter.flush();
                    CountZeroBitsByteProcessorHandler processorHandler = new CountZeroBitsByteProcessorHandler();
                    Thread thread = new Thread(new SocketReader(socket, processorHandler));
                    thread.start();
                    thread.join();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
