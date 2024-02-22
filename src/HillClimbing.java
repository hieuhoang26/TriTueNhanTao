import java.io.*;
import java.util.*;
import java.util.List;



public class HillClimbing {
    private Node<String, Integer> startVer, desVer;
    private static String filePath = "inputBestFS.txt";
    private static String filePathOut = "outputHillClimb.txt";

    public static void main(String[] args) {
        try {
            HillClimbing x = new HillClimbing();
            // Đọc file và xây dựng đồ thị
            Map<Node<String, Integer>, Map<String, Integer>> graph = x.readGraphFromFile(filePath);

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

    public Map<Node<String, Integer>, Map<String, Integer>> readGraphFromFile(String filePath) throws IOException {
        Map<Node<String, Integer>, Map<String, Integer>> graph = new HashMap<>();
        startVer = new Node<>();
        desVer = new Node<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Node<String, Integer> currentVertex = new Node<>();

            if ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] x1 = line.trim().split("\\s+");
                for (String i : x1) {
                    String[] x2 = i.split("-");
                    if (x2.length >= 2) {
                        if (startVer.isNull()) {
                            startVer = new Node<>(x2[0], Integer.valueOf(x2[1]));
                        } else {
                            desVer = new Node<>(x2[0], Integer.valueOf(x2[1]));
                        }
                    }
                }


                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        if (currentVertex.isNull()) {
                            String[] parts = line.split("-");
                            currentVertex.setFirst(parts[0]);
                            currentVertex.setSecond(Integer.valueOf(parts[1]));
                        } else {
                            String[] n1 = line.split("\\s+");
                            for (String i : n1) {
                                String[] n2 = i.split("-");
                                String neighborVertex = n2[0];
                                int weight = Integer.parseInt(n2[1]);

                                if (!graph.containsKey(currentVertex)) {
                                    graph.put(currentVertex, new HashMap<>());
                                }
                                graph.get(currentVertex).put(neighborVertex, weight);
                            }
                            currentVertex = new Node<>();
                        }
                    }
                }
            }
            return graph;
        }
    }

    public void printGraph(Map<Node<String, Integer>, Map<String, Integer>> graph) {
        System.out.println("start: " + startVer.toString());
        System.out.println("end: " + desVer.toString());
        for (Map.Entry<Node<String, Integer>, Map<String, Integer>> entry : graph.entrySet()) {
            Node<String, Integer> point = entry.getKey();
            Map<String, Integer> edges = entry.getValue();

            System.out.print(point.getFirst() + "-" + point.getSecond() + " : ");

            for (Map.Entry<String, Integer> edgeEntry : edges.entrySet()) {
                String neighbor = edgeEntry.getKey();
                int weight = edgeEntry.getValue();
                System.out.print(neighbor + "-" + weight + "  ");
            }
            System.out.println();
        }
    }

    public List<String> hillClimbing(Map<Node<String, Integer>, Map<String, Integer>> graph) {
        Map<Node<String, Integer>, Boolean> visited = new HashMap<>(); // đỉnh đã thăm
        Map<Node<String, Integer>, Node<String, Integer>> parent = new HashMap<>();  // child - parent

        Stack<Node<String, Integer>> L = new Stack<>();

        L.add(startVer);
        visited.put(startVer, true);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOut))) {
            writer.write(String.format("%-10s| %-20s| %-60s| %-40s\n", "Vertex", "Neighbors", "Visited", "In stack"));
            writer.write("-------------------------------------------------------------------------------------");
            writer.newLine();

            while (!L.isEmpty()) {
                Node<String, Integer> currPoint = L.pop();

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
                    Map<String, Integer> neighbors = graph.get(currPoint);
                    Map<String, Integer> sortedMap = neighbors.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .collect(
                                    LinkedHashMap::new, // Giữ thứ tự sắp xếp
                                    (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                                    LinkedHashMap::putAll // Put all vào Map mới
                            );

                    for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
                        Node<String, Integer> nextPoint = new Node<>(entry.getKey(), entry.getValue());
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
                        (graph.containsKey(currPoint) ? graph.get(currPoint).keySet() : ""),
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
    private int evaluateState(Node<String, Integer> state) {
        return state.getSecond(); // Đánh giá theo trọng số cạnh
    }


    public List<String> reconstructPath(Map<Node<String, Integer>, Node<String, Integer>> parent) {
        List<String> path = new ArrayList<>();
        Node<String, Integer> current = desVer;

        while (current != null) {
            path.add(current.getFirst());
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;
    }


}