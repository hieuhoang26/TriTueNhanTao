import java.io.*;
import java.util.*;


public class Astar {
    private Node startVer, desVer;
    private static String filePath = "inputA.txt";
    private static String filePathOut = "outputAstar.txt";

    public static void main(String[] args) {
        try {
            Astar x = new Astar();
            Map<Node, Map<Node, Integer>> graph = x.readGraphFromFile(filePath);

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

    public Map<Node, Map<Node, Integer>> readGraphFromFile(String filePath) throws IOException {
        Map<Node, Map<Node, Integer>> graph = new HashMap<>();
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
                    line = line.trim();

                    String[] list = line.split(" : ");
                    if (list.length >= 2) {
                        Node currentVertex = new Node();
                        currentVertex.readNode(list[0]);
                        Map<Node, Integer> neighbors = new HashMap<>();
                        String[] list2 = list[1].split("\\s+");
                        for (String i : list2) {
                            String[] parts = i.split("\\(");
                            String value = parts[1].replace(")", ""); //9
                            String key = parts[0]; //C-15
                            Node x = new Node();
                            x.readNode(key);
                            neighbors.put(x, Integer.valueOf(value));
                        }
                        graph.put(currentVertex, neighbors);
                    }



                }
            }
            return graph;
        }
    }

    public void printGraph(Map<Node, Map<Node, Integer>> graph) {
        System.out.println("start: " + startVer.toString());
        if (desVer != null) {
            System.out.println("end: " + desVer.toString());
        }
        for (Map.Entry<Node, Map<Node, Integer>> entry : graph.entrySet()) {
            Node vertex = entry.getKey();
            Map<Node, Integer> edges = entry.getValue();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(vertex.toString()).append(" : ");

            for (Map.Entry<Node, Integer> edgeEntry : edges.entrySet()) {
                Node neighbor = edgeEntry.getKey();
                int weight = edgeEntry.getValue();
                stringBuilder.append(neighbor.toString()).append("(").append(weight).append(")  ");
            }

            System.out.println(stringBuilder.toString());
        }
    }

    public List<String> bfs(Map<Node, Map<Node, Integer>> graph) {
        Map<String, String> parent = new HashMap<>();  // child - parent

        Map<String, Integer> fv = new HashMap<>();

        Integer costFinal = 0; // chi phi cuoi cung

        Queue<Node> queue = new LinkedList<>();

        // Thêm điểm bắt đầu vào hàng đợi
        queue.add(startVer);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOut))) {
            writer.write(String.format("%-5s| %-5s| %-8s| %-8s| %-8s| %-8s| %-40s\n", "TT", "TTK", "k(u,v)", "h(v)", "g(v)", "f(v)", "L"));
            writer.write("-------------------------------------------------------------------------------------");
            writer.newLine();

            while (!queue.isEmpty()) {
                Node currPoint = queue.poll();

                if ((currPoint.getFirst()).equals(desVer.getFirst())) {
                    writer.write(String.format("%-5s| %-5s| %-8s| %-8s| %-8s| %-8s| %-40s\n", desVer.getFirst(), "TT", "", "", "", "", ""));
                    writer.write("-------------------------------------------------------------------------------------");
                    writer.newLine();

                    List<String> path = reconstructPath(parent); // truy vết
                    writer.write("Cost: " + costFinal);
                    writer.newLine();
                    writer.write("Path:");
                    for (String i : path) {
                        writer.write(i + " ");
                    }
                    return path;
                }
                if (containNode(graph, currPoint.getFirst())) {
                    //Map<Node, Integer> neighbors = graph.get(currPoint);
                    Map<Node, Integer> neighbors = graph.get(getContainNode(graph, currPoint.getFirst()));

                    for (Map.Entry<Node, Integer> entry : neighbors.entrySet()) {
                        Integer k = 0; // chi phí k(u,v)
                        Integer h = 0; // h(x)
                        Integer f = 0; // f(x)
                        Integer g; // g(x)
                        if (currPoint.equals(desVer)) {
                            g = 0;
                        } else {
                            Integer gValue = fv.get(currPoint.getFirst());
                            g = gValue != null ? gValue : 0;
                        }

                        Node nextPoint = entry.getKey();
                        k = entry.getValue(); // chi phí

                        h = nextPoint.getSecond(); //h(x)
                        g = g + k;
                        f = g + h;
                        fv.put(nextPoint.getFirst(), g);
                        if ((nextPoint.getFirst()).equals(desVer.getFirst())) {
                            costFinal = g;
                        }
                        queue.add(new Node(nextPoint.getFirst(), f));
                        sortQueue(queue);
                        parent.put(nextPoint.getFirst(), currPoint.getFirst());

                        //Ghi thông tin vào file
                        writer.write(String.format("%-5s| %-5s| %-8s| %-8s| %-8s| %-8s| %-40s\n", currPoint.getFirst(),
                                nextPoint.getFirst(),
                                k, h, g, f,
                                queue.toString()
                        ));
                    }
                    writer.write("-------------------------------------------------------------------------------------");
                    writer.newLine();
                }

            }


        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private boolean containNode(Map<Node, Map<Node, Integer>> graph, String string) {
        for (Map.Entry<Node, Map<Node, Integer>> entry : graph.entrySet()) {
            Node key = entry.getKey();
            if (key.getFirst().equals(string)) {
                return true;
            }
        }
        return false;
    }

    private Node getContainNode(Map<Node, Map<Node, Integer>> graph, String string) {
        for (Map.Entry<Node, Map<Node, Integer>> entry : graph.entrySet()) {
            Node key = entry.getKey();
            if (key.getFirst().equals(string)) {
                return key;
            }
        }
        return null;
    }

    private void sortQueue(Queue<Node> queue) {
        List<Node> temp = new ArrayList<>();
        while (!queue.isEmpty()) {
            temp.add(queue.poll());
        }
        Collections.sort(temp, new NodeComparator());
        queue.clear();
        queue.addAll(temp);
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