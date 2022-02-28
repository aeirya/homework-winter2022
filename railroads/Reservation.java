package railroads;

public class Reservation {
    public final Rail rail;
    public final Train train;
    public final boolean isForward;
    public final int duration;
    private int timeRemaining;
    private boolean isActive;

    public Reservation(Rail rail, Train train, boolean isForward, int duration) {
        this.rail = rail;
        this.train = train;
        this.isForward = isForward;
        this.duration = duration;
        this.timeRemaining = duration;
    }

    public void decreaseTime() {
        timeRemaining -= train.velocity;
    }

    public boolean isOngoing() {
        return timeRemaining > 0 && isActive;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void active() {
        isActive = true;
    }

    public boolean isPending() {
        return !isActive;
    }

    public void print() {
        System.out.println("rail: " + rail.id + " train: " + train.id + " direction: " + (isForward?"forward":"backward"));
    }
}
