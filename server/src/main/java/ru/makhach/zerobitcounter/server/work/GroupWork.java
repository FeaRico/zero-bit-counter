package ru.makhach.zerobitcounter.server.work;

import ru.makhach.zerobitcounter.server.output.ResultChecker;
import ru.makhach.zerobitcounter.server.sender.TaskSender;

/**
 * Группа задач на выполнение
 */
public class GroupWork {
    private final TaskSender taskSender;
    private final ResultChecker resultChecker;

    public GroupWork(TaskSender taskSender, ResultChecker resultChecker) {
        this.taskSender = taskSender;
        this.resultChecker = resultChecker;
    }

    public TaskSender getTaskSender() {
        return taskSender;
    }

    public ResultChecker getResultChecker() {
        return resultChecker;
    }
}
