package railroads;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *   Contains the states of all rails and all approved rail reservations
 */
public class ReserveManager {
    /**
     *  maps rail id -> reservations for that id
     */
    private List<List<Reservation>> status;

    /**
     * @param n: number of rails
     */
    public ReserveManager(int n) {
        status = new ArrayList<>();
        for (int i=0; i<n; ++i) {
            status.add(new ArrayList<>());
        }
    }

    private boolean isFree(int rail) {
        var list = status.get(rail);
        if (list.isEmpty()) 
            return true;
        return list.stream().noneMatch(Reservation::isOngoing);
    }

    /**
     * returns whether current trains are (all) moving forward
     * 
     * ! only called when is busy (not free)
     */
    private boolean isBusyForward(int rail) {
        return status.get(rail).get(0).isForward;
    }

    public boolean canApprove(Reservation r) {
        if (isWillCollide(r.train, r.rail)) return false;
        if (!isRailAvailable(r.rail.id, r.isForward)) return false;
        admit(r);
        return true;
    }

    /**
     * returns whether the rail is directionally available for a train
     * 
     * a free rail means there is nothing to collide with
     * otherwise we need to check for head to head (here) or head to back collisions (in getLastReservation method)
     */
    private boolean isRailAvailable(int rail, boolean isTrainGoingForward) {
        if (isFree(rail)) 
            return true;

        if (isBusyForward(rail) != isTrainGoingForward)
            return false;  

        return true;
    }
    
    /** returns last reservation approved for a rail */
    private Reservation getLastReservation(int rail) {
        var list = status.get(rail);
        return list.get(list.size()-1);
    }
    
    /** checks whether there will be a collision with the last train in the rail (which is front to new trains) */
    private boolean isWillCollide(Train train, Rail rail) {
        // not implemented for simplification
        return false;
    }

    /**
     *  approves and makes new reservation active
     */
    private void admit(Reservation r) {
        status.get(r.rail.id).add(r);
    }

    /**
     * updates remaining time of every reservation
     */
    public void passTime() {
        // update times
        status.stream().parallel().forEach(l -> l.stream().forEach(Reservation::decreaseTime));

        // drop finished tasks
        status.stream().parallel().forEach(
            l -> l.removeIf(r -> !r.isOngoing())
        );
    }

    public void printStatus(List<Rail> rails, int maxRailSize) {
        for (int rail=0; rail<status.size(); ++rail) {
            System.out.print("rail " + ((rail < 9)?"0":"") + (rail+1) + ": ");
            var reserves = status.get(rail);
            var map = reserves.stream().collect(
                Collectors.toMap(
                    r -> {
                        int remDist = r.getTimeRemaining() * r.train.velocity;
                        if (!r.isForward) 
                            return remDist;
                        return maxRailSize - 1 - remDist;
                    }, 
                    r -> r.train.id
                    ));
            for (int i=0; i<maxRailSize; ++i) {
                if (map.containsKey(i)) {
                    int tid = map.get(i)+1; 
                    System.out.print(tid<10?"0":"" + tid);
                }
                else System.out.print("  ");
            }
            System.out.println();
        }
// 
    }

    // public void printStatus(List<Rail> rails, int maxRailSize) {
    //     int i=0, remTime, remDist, charIndex, remChars;
    //     for (int rail=0; rail<status.size(); ++rail) {
    //         System.out.print("rail " + ((rail < 9)?"0":"") + (rail+1) + ": ");
    //         var list = status.get(rail);
    //         String line = "";
    //         // remChars = maxRailSize;
    //         charIndex = 0;
    //         for (Reservation r : list) {
    //             remTime = r.getTimeRemaining();
    //             remDist = remTime * r.train.velocity;
    //             while (remDist >= charIndex) {
    //                 line = "  " + line;
    //                 ++charIndex;
    //             }
    //             if (r.train.id < 10) {
    //                 line = "0" + (r.train.id+1) + line;
    //             } else {
    //                 line = (r.train.id+1) + line;
    //             }
    //             ++charIndex;
    //         }
    //         while (charIndex < maxRailSize) {
    //             line = "  " + line;
    //             ++charIndex;
    //         }
    //         System.out.print(line);
    //         System.out.println();
    //     }
    // }

    public static void main(String[] args) {
        System.out.println("");
    }
    
}
