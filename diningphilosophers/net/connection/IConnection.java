package diningphilosophers.net.connection;

public interface IConnection extends IDataReceiver {
    void send(String message);
    void listen();
}
