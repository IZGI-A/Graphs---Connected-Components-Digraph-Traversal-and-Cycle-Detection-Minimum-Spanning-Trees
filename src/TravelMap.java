import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class TravelMap {

    // Maps a single Id to a single Location.
    public Map<Integer, Location> locationMap = new HashMap<>();

    // List of locations, read in the given order
    public List<Location> locations = new ArrayList<>();

    // List of trails, read in the given order
    public List<Trail> trails = new ArrayList<>();

    public Map<Location, Location> key = new HashMap<>();

    public void initializeMap(String filename) {
        // Read the XML file and fill the instance variables locationMap, locations and trails.
        try {
            DocumentBuilderFactory doc = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = doc.newDocumentBuilder();

            File f = new File(filename);
            Document document = docBuilder.parse(f);

            Element base = document.getDocumentElement();
            Element locItem= (Element) base.getElementsByTagName("Locations").item(0);
            Element trailsItem = (Element) base.getElementsByTagName("Trails").item(0);

            NodeList nodeList = locItem.getElementsByTagName("Location");
            for (int i=0; i< nodeList.getLength(); i++) {

                Node locnode = nodeList.item(i);
                if (locnode.getNodeType() == Node.ELEMENT_NODE) {
                    Element locationElement=(Element) locnode;

                    int id = Integer.parseInt(locationElement.getElementsByTagName("Id").item(0).getTextContent());
                    String kingName = locationElement.getElementsByTagName("Name").item(0).getTextContent();

                    Location loc = new Location(kingName, id);

                    locationMap.put(id, loc);
                    locations.add(loc);
                }
            }


            NodeList trailnodes = trailsItem.getElementsByTagName("Trail");
            for (int i= 0; i< trailnodes.getLength(); i++) {

                Node trailNode= trailnodes.item(i);
                if (trailNode.getNodeType() ==Node.ELEMENT_NODE) {
                    Element trailElement = (Element) trailNode;

                    int src= Integer.parseInt(trailElement.getElementsByTagName("Source").item(0).getTextContent());
                    int destination =Integer.parseInt(trailElement.getElementsByTagName("Destination").item(0).getTextContent());
                    int danger = Integer.parseInt(trailElement.getElementsByTagName("Danger").item(0).getTextContent());
                    Trail trail = new Trail(locationMap.get(src), locationMap.get(destination), danger);
                    trails.add(trail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Trail> getSafestTrails() {
        List<Trail> safestTrails = new ArrayList<>();
        // Fill the safestTrail list and return it.
        // Select the optimal Trails from the Trail list that you have read.
        for (int i =0; i<trails.size()-1; i++) {
            for (int j=0; j< trails.size() -i -1; j++) {
                if (trails.get(j).danger > trails.get(j+1).danger) {
                    Trail tmp = trails.get(j);
                    trails.set(j, trails.get(j+1));
                    trails.set(j+1, tmp);
                }
            }

        }
        for (Trail trail : trails) {
            // If source and destination belong to different set, we add the trail to the MST and union the sets
            if (look(trail.source, key) != look(trail.destination, key)) {
                Union( trail.source, trail.destination, key);
                safestTrails.add(trail);
            }
        }

        return safestTrails;
    }
    private Location look(Location loc, Map<Location, Location> key) {
        Location root = loc;
        while (key.get(root) != null) {
            root = key.get(root);
        }
        while (key.get(loc) != null) {
            Location temp = key.get(loc);
            key.put(loc, root);
            loc = temp;
        }
        return root;
    }
    private void Union(Location src, Location destination, Map<Location, Location> key) {
        key.put(look(src, key), look(destination, key));
    }


    public void printSafestTrails(List<Trail> safestTrails) {
        // Print the given list of safest trails conforming to the given output format.

        int total = 0;
        System.out.println("Safest trails are:");

        for (Trail trail : safestTrails) {
            System.out.println("The trail from " + trail.source.name + " to " + trail.destination.name + " with "+ trail.danger);
            total += trail.danger;
        }
        System.out.println("Total danger: " + total);
    }
}
