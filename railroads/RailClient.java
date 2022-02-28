package railroads;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

/** Each rail client is a train */
public class RailClient {
    private final RailsMonitor railsMonitor;    
    private final Train train;
    private final Random random;
    private final List<Rail> rails;
    private final int railsSize;

    private final Semaphore sem = new Semaphore(1);
    private Reservation reservation;

    private static final int MAX_WAIT_TIME = 3000;

    public RailClient(RailsMonitor railsMonitor, Train train, Random random, List<Rail> rails) {
        this.railsMonitor = railsMonitor;
        this.train = train;
        this.random = random;
        this.rails = rails;
        this.railsSize = rails.size();
    }
    
    public void run() {
        new Thread(
            () -> {
                while (true) {
                    int rail = random.nextInt(railsSize);
                    boolean direction = random.nextBoolean();
                    int duration = rails.get(rail).length / train.velocity;
                    
                    sleep();

                    // update reservation
                    try {
                        sem.acquire();
                        reservation = railsMonitor.request(train, rail, direction, duration);
                        sem.release();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    while (reservation.isPending()) {
                        // System.out.println("still pening is " + (train.id+1));
                        sleep();
                    }

                    while (reservation.isOngoing()) {
                        try {
                            Thread.sleep(
                                random.nextInt(MAX_WAIT_TIME)
                            );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        ).start();
    }

    private void sleep() {
        synchronized(railsMonitor) {
            try {
                railsMonitor.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Reservation getReservation() {
        try {
            sem.acquire();
            var r = reservation;
            sem.release();
            return r;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getState() {
        var r = getReservation();
        
        if (r == null) return "idle";

        if (r.isOngoing()) return "ongoing rail " + (r.rail.id+1);
        if (r.isPending()) return "waiting for rail " + (r.rail.id+1);
        
        return "idle";
    }

    public void printState() {
        System.out.println(
            "train " + (train.id+1) + ": " + getState()
            );
    }
}
