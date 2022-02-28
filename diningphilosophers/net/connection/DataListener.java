package diningphilosophers.net.connection;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class DataListener implements IDataListener {
    
    private final Socket socket;

    private IDataReceiver handler;
    private boolean isRunning;
    
    public DataListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void listen(IDataReceiver handler) {
        this.handler = handler;
        new Thread(this::run, "ConnectionDataListener").start();
    }

    private void run() {
        isRunning = true;
        try {
            final DataInputStream in = getInStream();

            while (isRunning) {
                final int len = in.readInt();
                final byte[] data = new byte[len];
                int count = 0;
                while (count < len) {
                    count += in.read(data, 0, len);
                }    
                handler.receive(data);
            }
        } catch(IOException e) {
            close();
            e.printStackTrace();
        }
    }

    private DataInputStream getInStream() throws IOException {
        return new DataInputStream(socket.getInputStream());
    }

    @Override
    public void close() {
        isRunning = false;
    }
    
}
