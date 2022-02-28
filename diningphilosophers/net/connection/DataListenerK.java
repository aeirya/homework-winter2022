package diningphilosophers.net.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class DataListenerK implements IDataListener {
    
    private final Socket socket;

    private IDataReceiver handler;
    private boolean isRunning;
    
    public DataListenerK(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void listen(IDataReceiver handler) {
        this.handler = handler;
        new Thread(this::run, "ConnectionDataListener").start();
    }

    private void run() {
        isRunning = true;
        Scanner scanner;
        try {
            scanner = new Scanner(socket.getInputStream());
            while (isRunning) {
                while (scanner.hasNextLine()) {
                    System.out.println("receive!");
                    String str = scanner.nextLine();
                    System.out.println(str);
                    handler.receive(
                        str.getBytes()
                        );
                }
            }
            scanner.close();
        } catch(IOException e) {
            close();
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        isRunning = false;
    }
    
}
