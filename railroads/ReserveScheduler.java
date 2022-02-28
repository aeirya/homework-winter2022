package railroads;

import java.util.stream.Collectors;

/** 
 * Contains all pending to be approved rail reservations
 */
public class ReserveScheduler {
    /**
     * List of Lists of Reservations for each rail id
     */
    private final NestedList<Reservation> pendings;
    
    /**
     * calls ReserveManager for canApprove method
     */
    private final ReserveManager reserves;
    
    public ReserveScheduler(int numberOfRails, ReserveManager reserves) {
        pendings = new NestedList<>(numberOfRails);
        this.reserves = reserves;
    }

    /**
     * pops best reservation pending for rail
     */
    public Reservation getNext(int rail) {
        var r = pendings.get(rail)
            .stream()
            .parallel()
            .filter(reserves::canApprove)
            .collect(Collectors.minBy((r1, r2)-> r1.duration - r2.duration))
            .orElse(null);
        pendings.get(rail).remove(r);
        if (r!=null) {
            r.active();
            r.print();
        }
        return r;
    }

    /** addes reservation to the pending */
    public void append(Reservation r) {
        pendings.get(r.rail.id).add(r);
    }
}
