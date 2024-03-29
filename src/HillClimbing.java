import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class HillClimbing {
    private Node startVer, desVer;
    private static String filePath = "inputBestFS.txt";
    private static String filePathOut = "outputHillClimb.txt";

    public static void main(String[] args) {
        try {
            HillClimbing x = new HillClimbing();
            // Đọc file và xây dựng đồ thị
            Map<Node, List<Node>> graph = x.readGraphFromFile(filePath);

            x.printGraph(graph);
            List<String> path = x.hillClimbing(graph);

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

    public List<String> hillClimbing(Map<Node, List<Node>> graph) {
        Map<Node, Boolean> visited = new HashMap<>(); // đỉnh đã thăm
        Map<Node, Node> parent = new HashMap<>();  // child - parent

        Stack<Node> L = new Stack<>();

        L.add(startVer);
        visited.put(startVer, true);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOut))) {
            writer.write(String.format("%-10s| %-20s| %-60s| %-40s\n", "Vertex", "Neighbors", "Visited", "In stack"));
            writer.write("-------------------------------------------------------------------------------------");
            writer.newLine();

            while (!L.isEmpty()) {
                Node currPoint = L.pop();

                if (currPoint.equals(desVer)) {
                    writer.write(String.format("%-10s| %-20s| %-60s| %-40s\n", desVer, "TT", "", ""));
                    writer.write("-------------------------------------------------------------------------------------");
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
                    List<Node> sortedNeighbors = neighbors.stream()
                            .sorted(Comparator.comparingInt(Node::getSecond).reversed())
                            .collect(Collectors.toList());

                    for (Node entry : sortedNeighbors) {
                        Node nextPoint = new Node(entry.getFirst(), entry.getSecond());
                        Boolean isVisited = visited.get(nextPoint);
                        if (isVisited == null || !isVisited) {
                            visited.put(nextPoint, true);
                            L.add(nextPoint);
                            parent.put(nextPoint, currPoint);
                        }
                    }

                }

                // Ghi thông tin vào file
                writer.write(String.format("%-10s| %-20s| %-60s| %-40s\n", currPoint.getFirst(),
                        (graph.containsKey(currPoint) ? graph.get(currPoint) : ""),
                        visited.keySet(),
                        L.toString()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Trả về một danh sách rỗng nếu không tìm thấy đường đi từ startNode đến desVer
        return Collections.emptyList();
    }

    // Đánh giá trạng thái
    private int evaluateState(Node state) {
        return state.getSecond(); // Đánh giá theo trọng số cạnh
    }


    public List<String> reconstructPath(Map<Node, Node> parent) {
        List<String> path = new ArrayList<>();
        Node current = desVer;

        while (current != null) {
            path.add(current.getFirst());
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;
    }


}