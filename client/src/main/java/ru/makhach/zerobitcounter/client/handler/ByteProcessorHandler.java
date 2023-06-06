package ru.makhach.zerobitcounter.client.handler;


public interface ByteProcessorHandler {
    /**
     * Обработка байт
     *
     * @return результат обработки
     */
    byte handleByte(byte b);
}
