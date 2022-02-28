package diningphilosophers.net.test;

import diningphilosophers.net.client.Connector;
import diningphilosophers.net.connection.Connection;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(5000);
        int port = 10000;
        Connection connection = new Connector(port).connect(m -> {
            System.out.println(m);
        });
        connection.listen();
        System.out.println("connect");
        
        // Thread.sleep(20000);
        // connection.listen();
        // connection.close();

        // System.exit(0);
    }

    static void onReceiveMessage(String message) {
        System.out.println(message);
    }
}
