package ru.makhach.zerobitcounter.server.output;

import ru.makhach.zerobitcounter.server.clean.Cleaner;
import ru.makhach.zerobitcounter.server.work.GroupWork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Поток, слушающий окончения результатов задач
 */
public class ResultChecker extends Thread {
    /**
     * Список {@link CompletableFuture}, ждущие результат выполнения вычисления экземпляром программы
     */
    private final List<CompletableFuture<Long>> futures;
    /**
     * Конечный результат
     */
    private final AtomicLong resultSum = new AtomicLong();
    private final ExecutorService executorService;
    /**
     * Путь до обрабатываемого файла
     */
    private final String path;
    private boolean runnable = true;

    public ResultChecker(String path) {
        this.path = path;
        futures = new CopyOnWriteArrayList<>();
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void run() {
        while (runnable) {
            checkFinish();
        }
    }

    /**
     * Добавление новой задачи
     *
     * @param socket задача, являющаяся сокетом
     */
    public void add(Socket socket) {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            try (InputStream inputStream = socket.getInputStream();) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String readLine = bufferedReader.readLine();
                return Long.parseLong(readLine);
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
            return 0L;
        }, executorService);
        this.futures.add(future);
    }

    /**
     * Проверяет, что задачи завершены. Суммирует результат всех задач и очищает {@link GroupWork}, когда все задачи завершены
     */
    private void checkFinish() {
        boolean ready = false;
        for (CompletableFuture<Long> next : futures) {
            if (next.isDone()) {
                try {
                    ready = true;
                    resultSum.addAndGet(next.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    ready = false;
                }
            } else ready = false;
        }
        if (ready) {
            runnable = false;
            System.out.println("RESULT FOR : " + path + " : " + resultSum.get());
            Cleaner.INSTANCE.clean(path);
            this.futures.clear();
        }
    }
}
