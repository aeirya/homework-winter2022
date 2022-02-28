package diningphilosophers.net.connection;

import java.io.IOException;
import java.net.Socket;

/**
 * Connection
 */
public class Connection implements IConnection {
    private final IMessageReceiver receiver;
    private final IDataSender sender;
    private final IDataListener listener;
    private final Socket socket;

    public Connection(IMessageReceiver receiver, Socket socket) {
        this(
            receiver,
            new DataSenderK(socket),
            new DataListenerK(socket),
            socket
            );
    }

    private Connection(IMessageReceiver receiver, IDataSender sender, IDataListener listener, Socket socket) {
        this.receiver = receiver;
        this.sender = sender;
        this.listener = listener;
        this.socket = socket;
    }

    @Override
    public void listen() {
        listener.listen(this);
    }

    @Override
    public void send(String data) {
        sender.send(data.getBytes());
    }

    @Override
    public void receive(byte[] data) {
        receiver.receive(new String(data));
    }

    public void close() {
        try {
            listener.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}