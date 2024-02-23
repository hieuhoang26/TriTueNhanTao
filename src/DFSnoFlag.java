import java.io.*;
import java.util.*;

public class DFSnoFlag {
    private String startVer, desVer;
    private static String filePath = "inputBFS.txt";
    private static String filePathOut = "outputDFSnoFlag.txt";

    public static void main(String[] args) {
        try {
            DFSnoFlag x = new DFSnoFlag();
            // Đọc file và xây dựng đồ thị
            Map<String, String[]> graph = x.readGraphFromFile(filePath);

            x.printGraph(graph);
            List<String> path = x.dfs(graph);

            if (!path.isEmpty()) {
                System.out.println("Shortest path: " + path);
            } else {
                System.out.println("No path found.");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> dfs(Map<String, String[]> graph) {

        Map<String, String> parent = new HashMap<>();  // child - parent

        Stack<String> stack = new Stack<>();
        stack.add(startVer);


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOut))) {
            writer.write(String.format("%-10s| %-20s| %-40s\n", "Vertex", "Neighbors", "In stack"));
            writer.write("-------------------------------------------------------------------------------------");
            writer.newLine();


            while (!stack.isEmpty()) {
                String currVer = stack.pop();

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
                            stack.add(near);
                            parent.put(near, currVer);
                    }
                }

                // Ghi thông tin vào file
                writer.write(String.format("%-10s| %-20s| %-40s\n", currVer,
                        (graph.containsKey(currVer) ? String.join(", ", graph.get(currVer)) : ""),
                        stack.toString()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
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
