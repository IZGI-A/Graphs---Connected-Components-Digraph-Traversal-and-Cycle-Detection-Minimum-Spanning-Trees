import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Kingdom {
    ArrayList<ArrayList<Integer>> graph;
    ArrayList<ArrayList<Integer>> undirected;
    int numberOfVertices;
    public void initializeKingdom(String filename) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                String[] rowItem = line.trim().split("\\s+");
                if (row == 0) {
                    numberOfVertices = rowItem.length;
                    graph = new ArrayList<>(numberOfVertices);
                    undirected = new ArrayList<>(numberOfVertices);

                    for (int i = 0; i < numberOfVertices; i++) {
                        graph.add(new ArrayList<>());
                        undirected.add(new ArrayList<>());
                    }

                }
                for (int column = 0; column < numberOfVertices; column++) {

                    if (Integer.parseInt(rowItem[column]) > 0) {
                        graph.get(row).add(column+1);
                        undirected.get(row).add(column);
                        undirected.get(column).add(row);
                    }
                }
                row++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private int[] id;
    private  int count;

    public List<Colony> getColonies() {
        List<Colony> colonies = new ArrayList<>();

        boolean[] visited = new boolean[numberOfVertices];
        id = new int[numberOfVertices];

        for (int i = 0; i < numberOfVertices; i++) {
            if (!visited[i]) {
                Colony colony = new Colony();
                dfs(i, visited);
                count++;
                colonies.add(colony);

            }
        }
        for (int i = 0; i< numberOfVertices; i++){
            colonies.get(id[i]).cities.add(i+1);

        }
        for (int i = 0; i < colonies.size(); i++) {
            Colony colony = colonies.get(i);
            for (int city : colony.cities) {
                colony.roadNetwork.put(city, graph.get(city-1));
            }
        }


        return colonies;
    }


    private void dfs(int city, boolean[] visited) {
        visited[city] = true;
        id[city] =count;
        for (int neighbor : undirected.get(city)) {
            if (!visited[neighbor]) {
                dfs(neighbor, visited);
            }
        }
    }
    public void printColonies(List<Colony> discoveredColonies) {
        // Print the given list of discovered colonies conforming to the given output format.

        // Print the adjacency list representation of the graph

        System.out.println("Discovered colonies are:");
        for (int i = 0; i < discoveredColonies.size(); i++) {
            Colony colony = discoveredColonies.get(i);
            System.out.println("Colony " + (i+1) + ": " + colony.cities);
        }

    }

}