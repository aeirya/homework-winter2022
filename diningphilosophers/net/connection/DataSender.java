package diningphilosophers.net.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DataSender implements IDataSender {

    private final Socket socket;

    public DataSender(Socket socket) {
        this.socket = socket;
    }

    private DataOutputStream getOutStream() throws IOException {
        return new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void send(byte[] data) {
        try {
            DataOutputStream out = getOutStream();
            out.writeInt(data.length);
            out.write(data);
            out.flush(); // 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
