package railroads;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
    Manages all requests for rails
*/
public class RailsMonitor {
    /*
        Rail Data Structure: id, length, isOneWay
    */
    private final List<Rail> rails;

    private final ReserveManager reserves;
    private final ReserveScheduler scheduler;

    public RailsMonitor(List<Rail> rails) {
        this.rails = rails;
        reserves = new ReserveManager(rails.size());
        scheduler = new ReserveScheduler(rails.size(), reserves);
    }

    /** 
     * locks until the request is given 
     * returns: 
    */
    public Reservation request(Train train, int rail, boolean direction, int duration) {
        var r = new Reservation(
            rails.get(rail),
            train, 
            direction, 
            duration);

        scheduler.append(r);

        return r;
    }
        
    public void passTime() {
        // updates all rails and trains
        reserves.passTime();
        
        // admits new trians
        IntStream
            .range(0, rails.size()).mapToObj(
            rail -> scheduler.getNext(rail)
        ).filter(Objects::nonNull)
        .map(r -> r.train)
        .forEach(t -> {
            synchronized(this) {
                this.notifyAll();
            }
        }
        );
    }

    static int maxRailSize = -1;
    public void printStatus() {
        if (maxRailSize == -1)
            maxRailSize = rails.stream().parallel().collect(Collectors.maxBy((r1,r2) -> r1.length - r2.length)).get().length;
        reserves.printStatus(rails, maxRailSize);
    }

}
