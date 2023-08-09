
import java.util.*;

public class TrapLocator {
    public List<Colony> colonies;
    private int V;

    public TrapLocator(List<Colony> colonies) {
        this.colonies = colonies;
    }

    public List<List<Integer>> revealTraps() {

        // Trap positions for each colony, should contain an empty array if the colony is safe.

        List<List<Integer>> traps = new ArrayList<>();

        // Identify the time traps and save them into the traps variable and then return it.
        for (Colony colony : colonies) {
            List<Integer> trapPstn = new ArrayList<>();

            Set<Integer> visited = new HashSet<>();
            Set<Integer> pathItem = new HashSet<>();

            for (int colon : colony.roadNetwork.keySet()) {

                if (isTrap(colon, colony.roadNetwork, visited, pathItem, trapPstn)) {
                    break;
                }
            }
            visited =new HashSet<>();
            if (trapPstn.size()>0)
                dfs(trapPstn.get(0), colony.roadNetwork, visited,trapPstn);

            Collections.sort(trapPstn) ;
            traps.add(trapPstn);

        }
        return traps;
    }
    public boolean flag= false;
    public boolean fl = false;
    private void dfs(int c, Map<Integer, List<Integer>> roadNetwork, Set<Integer> visited,List<Integer> trapPstn){

        visited.add(c);
        for(int city : roadNetwork.get(c)){
            if (!visited.contains(city)) {
                dfs(city, roadNetwork, visited,trapPstn);
            }
            else{
                flag=true;
                trapPstn.add(c);
                return;
            }

        }
        if (flag)return;

        visited.remove(c);
    }
    private boolean isTrap(int c, Map<Integer, List<Integer>> roadNetwork, Set<Integer> visited,
                           Set<Integer> path, List<Integer> trapPstn) {
        visited.add(c);
        path.add(c);
        for (int city: roadNetwork.get(c)) {

            if (!visited.contains(city)) {

                if (isTrap(city, roadNetwork, visited, path, trapPstn)) {return true;}
            }
            else if (path.contains(city)) {
                fl =true;
                trapPstn.add(city);
                return true;

            }

        }

        path.remove(c);
        return false;
    }

    public void printTraps(List<List<Integer>> traps) {
        // For each colony, if you have encountered a time trap, then print the cities that create the trap.
        // If you have not encountered a time trap in this colony, then print "Safe".
        // Print your findings conforming to the given output format.
        System.out.println("Danger exploration conclusions:");
        for (int i = 0; i < colonies.size(); i++) {

            List<Integer> trapPositions = traps.get(i);
            if (!trapPositions.isEmpty()) {
                System.out.println("Colony " + (i + 1) + ": Dangerous. Cities on the dangerous path: " + trapPositions);
            } else {
                System.out.println("Colony " + (i + 1) + ": Safe");
            }
        }
    }

}
