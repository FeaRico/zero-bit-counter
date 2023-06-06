package ru.makhach.zerobitcounter.server.reader;

public interface FileReader {
    /**
     * Чтение байт из ФС
     *
     * @param path путь до файла
     * @return массив прочитанных байт
     */
    byte[] readBytes(String path);
}
