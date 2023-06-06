package ru.makhach.zerobitcounter.server.input;

import ru.makhach.zerobitcounter.server.processor.PriorityBlockingQueueProcessor;
import ru.makhach.zerobitcounter.server.reader.BaseFileReader;
import ru.makhach.zerobitcounter.server.work.WorkService;
import ru.makhach.zerobitcounter.server.work.WorkServiceImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Поток, слушающий новые экземпляры
 */
public class SocketInput extends Thread {
    private final WorkService workService;

    public SocketInput() {
        workService = new WorkServiceImpl(new BaseFileReader(), new PriorityBlockingQueueProcessor());
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            while (true) {
                Socket socket = serverSocket.accept();
                workService.consume(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
