package ru.makhach.zerobitcounter.server;

import ru.makhach.zerobitcounter.server.input.SocketInput;

public class Server {
    public static void main(String[] args) {
        new SocketInput().start();
    }
}
