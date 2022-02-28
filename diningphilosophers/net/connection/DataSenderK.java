package diningphilosophers.net.connection;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class DataSenderK implements IDataSender {
    private PrintStream stream;

    public DataSenderK(Socket socket) {
        try {
            stream = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(byte[] data) {
        stream.println(new String(data));
        stream.flush();
    }
}
