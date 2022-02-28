package diningphilosophers.net.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

import diningphilosophers.net.connection.Connection;
import diningphilosophers.net.connection.IMessageReceiver;

public class ConnectionAcceptor {
    private static final String HOST = "localhost";
    private final int port;

    private final Consumer<Socket> onAccept;
    private ServerSocket socket;
    private boolean isRunning;

    public ConnectionAcceptor(int port, Consumer<Socket> onAccept) {
        this.port = port;
        this.onAccept = onAccept;
    }
    
    public void start() {
        try {
            socket = new ServerSocket(port);
            // socket.bind(new InetSocketAddress(HOST, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(this::run, "ConnectionAcceptor").start();
    }
    
    private void run() {
        isRunning = true;
        while (isRunning) {
            try (Socket client = socket.accept()) {
                System.out.println("accepted someone");
                onAccept.accept(client);
            } catch (IOException e) {
                e.printStackTrace();
                isRunning = false;
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        isRunning = false;
    }
}
