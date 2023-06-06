package ru.makhach.zerobitcounter.server.clean;

import ru.makhach.zerobitcounter.server.work.GroupWork;

import java.util.Map;

public enum Cleaner {
    INSTANCE;

    /**
     * Карта со всеми рабочими задачами
     */
    private Map<String, GroupWork> workMap;

    public void setWorkMap(Map<String, GroupWork> workMap) {
        this.workMap = workMap;
    }

    /**
     * Очистка завершенных задач
     *
     * @param path путь к файлу (выступает в роли идентификаторов задач)
     */
    public void clean(String path) {
        workMap.remove(path);
    }
}
