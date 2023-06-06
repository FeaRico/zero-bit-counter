package ru.makhach.zerobitcounter.server.work;

import ru.makhach.zerobitcounter.server.clean.Cleaner;
import ru.makhach.zerobitcounter.server.output.ResultChecker;
import ru.makhach.zerobitcounter.server.processor.QueueProcessor;
import ru.makhach.zerobitcounter.server.reader.FileReader;
import ru.makhach.zerobitcounter.server.sender.TaskSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class WorkServiceImpl implements WorkService {
    private final FileReader fileReader;
    private final QueueProcessor queueProcessor;
    private final Map<String, GroupWork> workMap;

    public WorkServiceImpl(FileReader fileReader, QueueProcessor queueProcessor) {
        this.fileReader = fileReader;
        this.queueProcessor = queueProcessor;
        workMap = new ConcurrentHashMap<>();
        Cleaner.INSTANCE.setWorkMap(workMap);
    }

    @Override
    public void consume(Socket socket) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line = bufferedReader.readLine();
        if (line != null) {
            GroupWork groupWork1 = workMap.compute(line, (key, groupWork) -> {
                if (groupWork == null) {
                    byte[] bytes = fileReader.readBytes(key);
                    Queue<Byte> queue = queueProcessor.getQueue(bytes);
                    GroupWork threadGroupWork = new GroupWork(new TaskSender(queue), new ResultChecker(key));
                    threadGroupWork.getTaskSender().start();
                    threadGroupWork.getResultChecker().start();
                    return threadGroupWork;
                } else return groupWork;
            });
            groupWork1.getTaskSender().addSender(socket);
            groupWork1.getResultChecker().add(socket);
        }
    }
}
