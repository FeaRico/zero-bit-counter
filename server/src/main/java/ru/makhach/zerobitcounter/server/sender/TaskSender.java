package ru.makhach.zerobitcounter.server.sender;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Поток, распределяющий нагрузку на все запущенные экземпляры
 */
public class TaskSender extends Thread {
    /**
     * Все экземпляры группы задач
     */
    private final List<Socket> sockets;
    /**
     * Очередь байт
     */
    private final Queue<Byte> queue;
    private volatile boolean runnable = true;

    public TaskSender(Queue<Byte> queue) {
        this.queue = queue;
        sockets = new CopyOnWriteArrayList<>();
    }

    /**
     * Отправка
     */
    private void send() throws IOException, InterruptedException {
        while (!queue.isEmpty()) {
            if (sockets.isEmpty())
                return;
            Iterator<Socket> iterator = sockets.iterator();
            while (iterator.hasNext()) {
                Socket socket = iterator.next();
                OutputStream outputStream = socket.getOutputStream();
                Byte poll = queue.poll();
                if (poll != null) {
//                    Thread.sleep(1000); // тут можно усыплять поток, чтобы успеть подключить новые экземпляры
                    outputStream.write(poll);
                    outputStream.flush();
                }
            }
        }

        shutdownAllSocketsOutput(); // Закрываем все выходные потоки сокетов, так как запись окончена

    }

    @Override
    public void run() {
        CompletableFuture.runAsync(() -> {
            while (runnable) {
                try {
                    send();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                stopSend();
            }
        });
    }

    /**
     * Добавление нового экземпляра в работу
     *
     * @param socket сокет экземпляра
     */
    public void addSender(Socket socket) {
        sockets.add(socket);
    }

    /**
     * Остановка отправки
     */
    public void stopSend() {
        this.runnable = false;
    }

    /**
     * Закрытие всех выходных потоков
     */
    private void shutdownAllSocketsOutput() {
        if (!sockets.isEmpty()) {
            for (Socket next : sockets) {
                try {
                    next.shutdownOutput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sockets.clear();
        }
    }
}
