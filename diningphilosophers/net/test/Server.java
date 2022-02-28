package diningphilosophers.net.test;

import java.net.Socket;

import diningphilosophers.net.connection.Connection;
import diningphilosophers.net.server.ConnectionAcceptor;

public class Server {
    public static void main(String[] args) throws InterruptedException {
        int port = 10000;
        var acceptor = new ConnectionAcceptor(port, Test::onAccept);
        acceptor.start();

        Thread.currentThread().sleep(60000);
        acceptor.close();

        System.exit(0);
    }

    static void onAccept(Socket socket) {
        var c = new Connection(null, socket);
        for (int i=0; i<5; ++i) {
            c.send("HI CLIENT!");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("SENT");
        c.close();
    }
}
