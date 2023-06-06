package ru.makhach.zerobitcounter.server.work;

import java.io.IOException;
import java.net.Socket;

public interface WorkService {
    /**
     * Обработка приёма сокет, добавление задач к уже существующим работающим задачам по пути
     *
     * @param socket
     * @throws IOException в случае ошибок чтения
     */
    void consume(Socket socket) throws IOException;
}
