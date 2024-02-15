import java.io.*;
import java.util.*;

class Point<T, U> {
    private T first;
    private U second;

    public Point() {
    }

    public Point(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return first + "-" + second;
    }

    public boolean isNull() {
        return (first == null && second == null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point<?, ?> point = (Point<?, ?>) o;
        return Objects.equals(first, point.first) &&
                Objects.equals(second, point.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}

public class HillClimbing {
    private Point<String, Integer> startVer, desVer;
    private static String filePath = "inputBestFS.txt";
    private static String filePathOut = "outputHillClimb.txt";

    public static void main(String[] args) {
        try {
            HillClimbing x = new HillClimbing();
            // Đọc file và xây dựng đồ thị
            Map<Point<String, Integer>, Map<String, Integer>> graph = x.readGraphFromFile(filePath);

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

    public Map<Point<String, Integer>, Map<String, Integer>> readGraphFromFile(String filePath) throws IOException {
        Map<Point<String, Integer>, Map<String, Integer>> graph = new HashMap<>();
        startVer = new Point<>();
        desVer = new Point<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Point<String, Integer> currentVertex = new Point<>();

            if ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] x1 = line.trim().split("\\s+");
                for (String i : x1) {
                    String[] x2 = i.split("-");
                    if (x2.length >= 2) {
                        if (startVer.isNull()) {
                            startVer = new Point<>(x2[0], Integer.valueOf(x2[1]));
                        } else {
                            desVer = new Point<>(x2[0], Integer.valueOf(x2[1]));
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
                            currentVertex = new Point<>();
                        }
                    }
                }
            }
            return graph;
        }
    }

    public void printGraph(Map<Point<String, Integer>, Map<String, Integer>> graph) {
        System.out.println("start: " + startVer.toString());
        System.out.println("end: " + desVer.toString());
        for (Map.Entry<Point<String, Integer>, Map<String, Integer>> entry : graph.entrySet()) {
            Point<String, Integer> point = entry.getKey();
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

    public List<String> hillClimbing(Map<Point<String, Integer>, Map<String, Integer>> graph) {
        Map<Point<String, Integer>, Boolean> visited = new HashMap<>(); // đỉnh đã thăm
        Map<Point<String, Integer>, Point<String, Integer>> parent = new HashMap<>();  // child - parent

        Stack<Point<String, Integer>> L = new Stack<>();

        L.add(startVer);
        visited.put(startVer, true);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOut))) {
            writer.write(String.format("%-10s| %-20s| %-60s| %-40s\n", "Vertex", "Neighbors", "Visited", "In stack"));
            writer.write("-------------------------------------------------------------------------------------");
            writer.newLine();

            while (!L.isEmpty()) {
                Point<String, Integer> currPoint = L.pop();

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
                        Point<String, Integer> nextPoint = new Point<>(entry.getKey(), entry.getValue());
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
    private int evaluateState(Point<String, Integer> state) {
        return state.getSecond(); // Đánh giá theo trọng số cạnh
    }


    public List<String> reconstructPath(Map<Point<String, Integer>, Point<String, Integer>> parent) {
        List<String> path = new ArrayList<>();
        Point<String, Integer> current = desVer;

        while (current != null) {
            path.add(current.getFirst());
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;
    }


}