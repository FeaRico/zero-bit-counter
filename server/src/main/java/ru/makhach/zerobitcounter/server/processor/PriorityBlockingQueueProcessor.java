package ru.makhach.zerobitcounter.server.processor;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Базовая реализация с блокирующей приоритетной очередью
 */
public class PriorityBlockingQueueProcessor implements QueueProcessor {
    @Override
    public Queue<Byte> getQueue(byte[] bytes) {
        Queue<Byte> queue = new PriorityBlockingQueue<>();
        for (byte b : bytes) {
            queue.add(b);
        }
        return queue;
    }
}
