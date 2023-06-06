package ru.makhach.zerobitcounter.server.processor;

import java.util.Queue;

public interface QueueProcessor {
    /**
     * Получение очереди байтов из массива
     *
     * @param bytes массив байт
     * @return очередь
     */
    Queue<Byte> getQueue(byte[] bytes);
}
