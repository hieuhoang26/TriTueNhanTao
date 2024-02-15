import java.io.*;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class BFSnoFlag {
    private String startVer, desVer;
    private Map<String, String[]> graph = new HashMap<>();

    private static String filePath = "inputBFS.txt";
    private static String filePathOut = "outputBFSnoFlag.txt";

    public static void main(String[] args) {
        try {


            BFSnoFlag x = new BFSnoFlag();
            // Đọc file và xây dựng đồ thị
            x.graph = x.readGraphFromFile(filePath);

            x.printGraph(x.graph);
            List<String> path = x.bfsNoFlag(x.graph);

            if (!path.isEmpty()) {
                System.out.println("Shortest path: " + path);
            } else {
                System.out.println("No path found.");
            }
            //writeGraphToFile(graph,filePathOut);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String[]> readGraphFromFile(String filePath) throws IOException {
        Map<String, String[]> graph = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentVertex = null;
            if ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] x = line.split("\\s+");
                startVer = x[0];
                desVer = x[1];
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        if (currentVertex == null) {
                            currentVertex = line;
                        } else {
                            String[] neighbors = line.split("\\s+");
                            graph.put(currentVertex, neighbors);
                            currentVertex = null;
                        }
                    }
                }
            }
            return graph;
        }
    }

    public List<String> bfsNoFlag(Map<String, String[]> graph) {

        Map<String, String> parent = new HashMap<>();  // child - parent

        Queue<String> queue = new LinkedList<>();
        queue.add(startVer);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOut))) {
            writer.write(String.format("%-10s| %-20s| %-40s\n", "Vertex", "Neighbors", "In Queue"));
            writer.write("-------------------------------------------------------------------------------------");
            writer.newLine();
            while (!queue.isEmpty()) {
                String currVer = queue.poll();

                if (currVer.equals(desVer)) {
                    writer.write(String.format("%-10s| %-20s| %-40s\n", desVer, "TT", ""));
                    writer.write("-------------------------------------------------------------------------------------");
                    writer.newLine();

                    List<String> path = reconstructPath(parent); // truy vết
                    writer.write("Path:" );
                    for ( String i : path) {
                        writer.write(i +" ");
                    }
                    return path;
                }

                if (graph.containsKey(currVer)) {
                    for (String near : graph.get(currVer)) {

                        queue.add(near);
                        parent.put(near, currVer);

                    }
                }
                writer.write(String.format("%-10s| %-20s| %-40s\n", currVer,
                        (graph.containsKey(currVer) ? String.join(", ", graph.get(currVer)) : ""),

                        queue.toString()
                ));
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Collections.emptyList();
    }

    public List<String> reconstructPath(Map<String, String> parent) {
        List<String> path = new ArrayList<>();
        String current = desVer;

        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    public void printGraph(Map<String, String[]> graph) {
        System.out.println("start: " + startVer);
        System.out.println("end: " + desVer);
        for (Map.Entry<String, String[]> entry : graph.entrySet()) {
            String vertex = entry.getKey();
            String[] neighbors = entry.getValue();

            System.out.print(vertex + ": ");

            for (String neighbor : neighbors) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }



}