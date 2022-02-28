package railroads;

public class Rail {
    public final int id;
    public final int length;
    public final boolean oneway;

    public Rail(int id, int length, boolean oneway) {
        this.id = id;
        this.length = length;
        this.oneway = oneway;
    }
}
