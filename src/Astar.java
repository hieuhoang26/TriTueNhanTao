import java.io.*;
import java.util.*;


public class Astar {
    private Node<String, Integer> startVer, desVer;
    private static String filePath = "inputA.txt";
    private static String filePathOut = "outputAstar.txt";

    public static void main(String[] args) {
        try {
            Astar x = new Astar();
            // Đọc file và xây dựng đồ thị
            Map<Node<String, Integer>, Map<Node<String, Integer>, Integer>> graph = x.readGraphFromFile(filePath);

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

    public Map<Node<String, Integer>, Map<Node<String, Integer>, Integer>> readGraphFromFile(String filePath) throws IOException {
        Map<Node<String, Integer>, Map<Node<String, Integer>, Integer>> graph = new HashMap<>();
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
                            // Nếu chưa có currentVertex, đọc đỉnh mới
                            String[] parts = line.split("-");
                            currentVertex.setFirst(parts[0]);
                            currentVertex.setSecond(Integer.valueOf(parts[1]));
                        } else {
                            // Đọc các đỉnh kề và trọng số của currentVertex
                            String[] neighbors = line.split("\\s+");
//C-15(9)
                            for (String neighbor : neighbors) {
                                String[] parts = neighbor.split("\\(");
                                //C-15 //9)
                                String key = parts[0]; //C-15
                                String value = parts[1].replace(")", ""); //9
                                //C 15
                                String[] n2 = key.split("-");
                                String neighborVertex = n2[0];
                                int weight = Integer.parseInt(n2[1]);
                                Node<String, Integer> x = new Node<>(neighborVertex, weight);
                                if (!graph.containsKey(currentVertex)) {
                                    graph.put(currentVertex, new HashMap<>());
                                }
                                graph.get(currentVertex).put(x, Integer.parseInt(value));
                            }
                            currentVertex = new Node<>();
                        }
                    }
                }
            }
            return graph;
        }
    }

    public void printGraph(Map<Node<String, Integer>, Map<Node<String, Integer>, Integer>> graph) {
        System.out.println("start: " + startVer.toString());
        if (desVer != null) {
            System.out.println("end: " + desVer.toString());
        }
        for (Map.Entry<Node<String, Integer>, Map<Node<String, Integer>, Integer>> entry : graph.entrySet()) {
            Node<String, Integer> vertex = entry.getKey();
            Map<Node<String, Integer>, Integer> edges = entry.getValue();

            System.out.print(vertex.toString() + " : ");

            for (Map.Entry<Node<String, Integer>, Integer> edgeEntry : edges.entrySet()) {
                Node<String, Integer> neighbor = edgeEntry.getKey();
                int weight = edgeEntry.getValue();
                System.out.print(neighbor.toString() + "(" + weight + ")" + "  ");
            }
            System.out.println();
        }
    }

    public List<String> bfs(Map<Node<String, Integer>, Map<Node<String, Integer>, Integer>> graph) {
        //Map<Point<String, Integer>, Point<String, Integer>> parent = new HashMap<>();  // child - parent
        Map<String, String> parent = new HashMap<>();  // child - parent



        PriorityQueue<Node<String, Integer>> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getSecond));


        // Thêm điểm bắt đầu vào hàng đợi
        queue.add(startVer);
        //visited.put(startVer, true);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOut))) {
            writer.write(String.format("%-5s| %-5s| %-8s| %-8s| %-8s| %-8s| %-40s\n", "TT", "TTK", "k(u,v)", "h(v)","g(v)", "f(v)", "L"));
            writer.write("-------------------------------------------------------------------------------------");
            writer.newLine();

            while (!queue.isEmpty()) {
                Node<String, Integer> currPoint = queue.poll();

                if (currPoint.equals(desVer)) {
                    writer.write(String.format("%-5s| %-5s| %-8s| %-8s| %-8s| %-8s| %-40s\n", desVer, "TT", "", ""));
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
                    Map<Node<String, Integer>, Integer> neighbors = graph.get(currPoint);

                    for (Map.Entry<Node<String, Integer>, Integer> entry : neighbors.entrySet()) {
                        Integer k=0; // chi phí k(u,v)
                        Integer h=0; // h(x)
                        Integer f=0; // f(x)
                        Integer g = 0; // g(x)

                        Node<String, Integer> nextPoint = entry.getKey();
                        k = entry.getValue(); // chi phí

                        h = nextPoint.getSecond(); //h(x)
                        g = g + k;
                        f = g + h;
                        queue.add(new Node<>(nextPoint.getFirst(), f));
                        parent.put(nextPoint.getFirst(), currPoint.getFirst());

                        //Ghi thông tin vào file
                        writer.write(String.format("%-5s| %-5s| %-8s| %-8s| %-8s| %-8s| %-40s\n", currPoint.getFirst(),
                                nextPoint.getFirst(),
                                k,h,g,f,
                                queue.toString()
                        ));
                    }


                }

            }


        } catch (
                IOException e) {
            e.printStackTrace();
        }

        // Trả về một danh sách rỗng nếu không tìm thấy đường đi từ startPoint đến desVer
        return Collections.emptyList();
    }

    private int smallestEdgeWeight(Node<String, Integer> Point) {
        return Point.getSecond();
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


}