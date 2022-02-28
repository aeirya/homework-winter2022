package diningphilosophers.net.test;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import diningphilosophers.net.client.Connector;
import diningphilosophers.net.connection.Connection;
import diningphilosophers.net.server.ConnectionAcceptor;

public class Test {

    static Semaphore sem = new Semaphore(1);
    public static void main(String[] args) throws InterruptedException {
        int port = 8081;
        sem.acquire();

        var acceptor = new ConnectionAcceptor(port, Test::onAccept);
        acceptor.start();

        
        for (int i=0; i<10000; ++i) {}

        var connection = new Connector(port).connect(m -> onReceiveMessage(m));
        connection.listen();

        sem.release();

        System.out.println("connected");


        connection.close();
        acceptor.close();
    }

    static void onAccept(Socket socket) {
        var c = new Connection(null, socket);
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        c.send("HI CLIENT!");
        System.out.println("SENT");
        c.close();
        sem.release();
    }

    static void onReceiveMessage(String message) {
        System.out.println(message);
    }
}
