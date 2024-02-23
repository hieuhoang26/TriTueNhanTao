import java.io.*;
import java.net.Inet4Address;
import java.util.*;


public class BestFirstSearch {
    private Node<String, Integer> startVer, desVer;
    private static String filePath = "inputBestFS.txt";
    private static String filePathOut = "outputBestFS.txt";

    public static void main(String[] args) {
        try {
            BestFirstSearch x = new BestFirstSearch();
            // Đọc file và xây dựng đồ thị
            Map<Node<String, Integer>, Map<String, Integer>> graph = x.readGraphFromFile(filePath);

            x.printGraph(graph);
            List<String> path = x.bfs(graph);

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
            Node<String, Integer> Point = entry.getKey();
            Map<String, Integer> edges = entry.getValue();

            System.out.print(Point.getFirst() + "-" + Point.getSecond() + " : ");

            for (Map.Entry<String, Integer> edgeEntry : edges.entrySet()) {
                String neighbor = edgeEntry.getKey();
                int weight = edgeEntry.getValue();
                System.out.print(neighbor + "-" + weight + "  ");
            }
            System.out.println();
        }
    }

    public List<String> bfs(Map<Node<String, Integer>, Map<String, Integer>> graph) {
        Map<Node<String, Integer>, Boolean> visited = new HashMap<>(); // đỉnh đã thăm
        Map<String, String> parent = new HashMap<>();  // child - parent

        Queue<Node<String, Integer>> queue = new LinkedList<>();

        // Thêm điểm bắt đầu vào hàng đợi
        queue.add(startVer);
        visited.put(startVer, true);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOut))) {
            writer.write(String.format("%-10s| %-20s| %-60s| %-40s\n", "Vertex", "Neighbors", "Visited", "In Queue"));
            writer.write("-------------------------------------------------------------------------------------");
            writer.newLine();

            while (!queue.isEmpty()) {
                Node<String, Integer> currPoint = queue.poll();

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
                    for (Map.Entry<String, Integer> entry : neighbors.entrySet()) {
                        Node<String, Integer> nextPoint = new Node<>(entry.getKey(), entry.getValue());
                        Boolean isVisited = visited.get(nextPoint);
                        if (isVisited == null || !isVisited) {
                            visited.put(nextPoint, true);
                            queue.add(nextPoint);
                            sortQueue(queue);
                            parent.put(nextPoint.getFirst(), currPoint.getFirst());
                        }
                    }


                }
                //             Ghi thông tin vào file
                writer.write(String.format("%-10s| %-20s| %-60s| %-40s\n", currPoint.getFirst(),
                        (graph.containsKey(currPoint) ? graph.get(currPoint).keySet() : ""),
                        visited.keySet(),
                        queue.toString()
                ));
            }


        } catch (
                IOException e) {
            e.printStackTrace();
        }

        // Trả về một danh sách rỗng nếu không tìm thấy đường đi từ startPoint đến desVer
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
    private void sortQueue(Queue<Node<String,Integer>> queue){
        List<Node<String, Integer>> temp = new ArrayList<>();
        while (!queue.isEmpty()){
            temp.add(queue.poll());
        }
        Collections.sort(temp,Comparator.comparingInt(this::smallestEdgeWeight));
        queue.clear();
        queue.addAll(temp);
    }
    private int smallestEdgeWeight(Node<String, Integer> Point) {
        return Point.getSecond();
    }


}