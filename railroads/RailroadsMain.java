package railroads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Main Program
 */
public class RailroadsMain {
    public static void main(String[] args) {
        List<Rail> rails = new ArrayList<>();
        List<Train> trains = new ArrayList<>();
        getInput(rails, trains);

        Random random = new Random();
        RailsMonitor monitor = new RailsMonitor(rails);
        
        List<RailClient> clients = new ArrayList<>();
        for (Train train : trains) {
            clients.add(
                new RailClient(monitor, train, random, rails));
        }

        // run clients
        clients.stream().forEach(RailClient::run);
            
        while (true) {
            // print state of trains
            for (RailClient client : clients) {
                client.printState();
            }

            System.out.println();

            // print state of rails
            monitor.printStatus();

            System.out.println("\n-----------------------\n");
            monitor.passTime();
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /*
        gets text input and saves in rails and trains list 
    */
    static void getInput(List<Rail> rails, List<Train> trains) {
        var scanner = new Scanner(System.in);
        int
            n = scanner.nextInt(),
            m = scanner.nextInt();
        
    
        // input rails
        for (int i=0; i<n; ++i) {
            rails.add(
                new Rail(
                    scanner.nextInt()-1,
                    scanner.nextInt(),
                    scanner.nextInt() == 0
                )
            );
        }
    
        // input trains
        for (int i=0; i<m; ++i) {
            trains.add(
                new Train(
                    scanner.nextInt()-1, 
                    scanner.nextInt()
                    )
            );
        }
    
        scanner.close();
    }
}
