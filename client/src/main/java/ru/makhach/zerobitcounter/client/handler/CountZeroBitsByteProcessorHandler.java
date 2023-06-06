package ru.makhach.zerobitcounter.client.handler;

/**
 * Реализация обработчика байт, который считает количество нулевых бит
 */
public class CountZeroBitsByteProcessorHandler implements ByteProcessorHandler {
    @Override
    public byte handleByte(byte b) {
        byte count = 0;
        for (int i = 0; i < 8; i++) {
            if ((b & (1 << i)) == 0)
                count++;
        }
        return count;
    }
}
