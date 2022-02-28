package diningphilosophers.net.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import diningphilosophers.net.connection.Connection;
import diningphilosophers.net.connection.IMessageReceiver;

public class Connector {
    
    private final String host;
    private final int port;

    public Connector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Connector(int port) {
        this("localhost", port);
    }

    public Connection connect(IMessageReceiver receiver) {
        try {
            Socket socket = new Socket(host, port);
            return new Connection(receiver, socket);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
