package diningphilosophers.net.connection;

public interface IDataListener extends java.io.Closeable {
    void listen(IDataReceiver receiver);
}
