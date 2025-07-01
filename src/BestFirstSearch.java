import java.io.*;
import java.net.Inet4Address;
import java.util.*;


public class BestFirstSearch {
    private Node startVer, desVer;
    private static String filePath = "inputBestFS.txt";
    private static String filePathOut = "outputBestFS.txt";

    public static void main(String[] args) {
        try {
            BestFirstSearch x = new BestFirstSearch();
            // Đọc file và xây dựng đồ thị
            Map<Node,List<Node>> graph = x.readGraphFromFile(filePath);

            x.printGraph(graph);
            List<String> path = x.bestfs(graph);

            if (!path.isEmpty()) {
                System.out.println("Shortest path: " + path);
            } else {
                System.out.println("No path found.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Node, List<Node>> readGraphFromFile(String filePath) throws IOException {

        Map<Node, List<Node>> graph = new HashMap<>();
        startVer = new Node();
        desVer = new Node();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            if ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] x1 = line.trim().split("\\s+");
                for (String i : x1) {
                    String[] x2 = i.split("-");
                    if (x2.length >= 2) {
                        if (startVer.isNull()) {
                            startVer = new Node(x2[0], Integer.valueOf(x2[1]));
                        } else {
                            desVer = new Node(x2[0], Integer.valueOf(x2[1]));
                        }
                    }
                }

                while ((line = reader.readLine()) != null) {
                    Node currentVertex = new Node();
                    List<Node> neighbors = new ArrayList<>();

                    line = line.trim();
                    String[] list = line.split(" : ");
                    currentVertex.readNode(list[0]);
                    String[] list2 = list[1].split("\\s+");
                    for (String i : list2) {
                        Node x = new Node();
                        x.readNode(i);
                        neighbors.add(x);
                    }
                    graph.put(currentVertex,neighbors);
                }
            }
            return graph;
        }
    }

    public void printGraph(Map<Node, List<Node>> graph) {
        System.out.println("start: " + startVer.toString());
        System.out.println("end: " + desVer.toString());

        for (Map.Entry<Node, List<Node>> entry : graph.entrySet()) {
            Node node = entry.getKey();
            List<Node> neighbors = entry.getValue();

            System.out.print(node + " : ");

            for (Node neighbor : neighbors) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }

    public List<String> bestfs(Map<Node, List<Node>> graph) {
        Map<String, String> parent = new HashMap<>();  // child - parent
        Map<Node, Boolean> visited = new HashMap<>();

        Queue<Node> queue = new LinkedList<>();


        queue.add(startVer);
        visited.put(startVer,true);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOut))) {
            writer.write(String.format("%-10s| %-20s| %-60s| %-40s\n", "Vertex", "Neighbors","Visited", "In Queue"));
            writer.write("-------------------------------------------------------------------------------------------------------");
            writer.newLine();

            while (!queue.isEmpty()) {
                Node currPoint = queue.poll();

                if (currPoint.equals(desVer)) {
                    writer.write(String.format("%-10s| %-20s| %-60s| %-40s\n", desVer.getFirst(), "TT","", ""));
                    writer.write("--------------------------------------------------------------------------------------------------");
                    writer.newLine();

                    List<String> path = reconstructPath(parent); // truy vết
                    writer.write("Path:");
                    for (String i : path) {
                        writer.write(i + " ");
                    }
                    return path;
                }

                if (graph.containsKey(currPoint)) {
                    List<Node> neighbors = graph.get(currPoint);
                    for (Node entry : neighbors) {
                        Node nextPoint = new Node(entry.getFirst(), entry.getSecond());
                        Boolean isVisited = visited.get(nextPoint);
                        if(isVisited == null || !isVisited){
                            visited.put(nextPoint,true);
                            queue.add(nextPoint);
                            sortQueue(queue);
                            parent.put(nextPoint.getFirst(), currPoint.getFirst());
                        }
                    }
                }
                writer.write(String.format("%-10s| %-20s| %-60s| %-40s\n", currPoint.getFirst(),
                        (graph.containsKey(currPoint) ? graph.get(currPoint) : ""),
                        visited.keySet(),
                        queue.toString()
                ));
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<String> reconstructPath(Map<String, String> parent) {
        List<String> path = new ArrayList<>();
        String current = desVer.getFirst();

        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;
    }
    private void sortQueue(Queue<Node> queue){
        List<Node> temp = new ArrayList<>();
        while (!queue.isEmpty()){
            temp.add(queue.poll());
        }
        Collections.sort(temp,Comparator.comparingInt(this::smallestEdgeWeight));
        queue.clear();
        queue.addAll(temp);
    }
    private int smallestEdgeWeight(Node Point) {
        return Point.getSecond();
    }


}