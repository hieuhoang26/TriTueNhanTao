import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class NhanhVsCan {
    private Node startVer, desVer;
    private static String filePath = "inputA.txt";
    private static String filePathOut = "outputNhanhCan.txt";

    public static void main(String[] args) {
        try {
            NhanhVsCan x = new NhanhVsCan();
            // Đọc file và xây dựng đồ thị
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
            Node currentVertex = new Node();

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
                                Node x = new Node(neighborVertex, weight);
                                if (!graph.containsKey(currentVertex)) {
                                    graph.put(currentVertex, new HashMap<>());
                                }
                                graph.get(currentVertex).put(x, Integer.parseInt(value));
                            }
                            currentVertex = new Node();
                        }
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

            System.out.print(vertex.toString() + " : ");

            for (Map.Entry<Node, Integer> edgeEntry : edges.entrySet()) {
                Node neighbor = edgeEntry.getKey();
                int weight = edgeEntry.getValue();
                System.out.print(neighbor.toString() + "(" + weight + ")" + "  ");
            }
            System.out.println();
        }
    }

    public List<String> bfs(Map<Node, Map<Node, Integer>> graph) {
        //Map<Point<String, Integer>, Point<String, Integer>> parent = new HashMap<>();  // child - parent
        Map<String, String> parent = new HashMap<>();  // child - parent
        Map<String, Integer> fv = new HashMap<>();

        Integer costFinal = 0;


        Stack<Node> stackL = new Stack<>();

        // Thêm điểm bắt đầu vào hàng đợi
        stackL.add(startVer);


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOut))) {
            writer.write(String.format("%-5s| %-5s| %-8s| %-8s| %-8s| %-8s| %-30s| %-40s\n", "TT", "TTK", "k(u,v)", "h(v)", "g(v)", "f(v)", "L1", "L"));
            writer.write("------------------------------------------------------------------------------------------------------------");
            writer.newLine();

            while (!stackL.isEmpty()) {
                Node currPoint = stackL.pop();
                Stack<Node> stackCopy = new Stack<>();
                stackCopy.addAll(stackL);
                System.out.println(stackCopy.toString());
                System.out.println(costFinal);
                //if ((currPoint.getFirst()).equals(desVer.getFirst())  && checkEnd(stackCopy,costFinal)) {
                if ((currPoint.getFirst()).equals(desVer.getFirst()) ) {
                    writer.write(String.format("%-5s| %-5s| %-8s| %-8s| %-8s| %-8s| %-30s| %-40s\n", desVer.getFirst(), "TT", "", "", "", costFinal, "", ""));
                    writer.write("------------------------------------------------------------------------------------------------------------");
                    writer.newLine();

                    if(checkEnd(stackCopy,costFinal)){
//                        writer.write(String.format("%-5s| %-5s| %-8s| %-8s| %-8s| %-8s| %-30s| %-40s\n", desVer.getFirst(), "TT", "", "", "", "", "", ""));
//                        writer.write("------------------------------------------------------------------------------------------------------------");
//                        writer.newLine();

                        List<String> path = reconstructPath(parent); // truy vết
                        writer.write("Cost: " + costFinal);
                        writer.newLine();
                        writer.write("Path:");
                        for (String i : path) {
                            writer.write(i + " ");
                        }
                        return path;
                    }
                }


                if (containNode(graph, currPoint.getFirst())) {

                    Map<Node, Integer> neighbors = graph.get(getContainNode(graph, currPoint.getFirst()));

                    List<Node> listL1 = new ArrayList<>();

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

                        listL1.add(new Node(nextPoint.getFirst(), f));

                        parent.put(nextPoint.getFirst(), currPoint.getFirst());
                        //Ghi thông tin vào file
                        writer.write(String.format("%-5s| %-5s| %-8s| %-8s| %-8s| %-8s| %-30s| %-40s\n", currPoint.getFirst(),
                                nextPoint.getFirst(),
                                k, h, g, f,
                                "", ""
                        ));
                    }
                    Collections.sort(listL1, new NodeComparator());
                    Collections.reverse(listL1);
                    stackL.addAll(listL1);
                    writer.write(String.format("%-5s| %-5s| %-8s| %-8s| %-8s| %-8s| %-30s| %-40s\n", "",
                            "", "", "", "", "",
                            listL1.toString(),
                            stackL.toString()
                    ));
                    writer.write("------------------------------------------------------------------------------------------------------------");
                    writer.newLine();
                }

            }


        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    private boolean checkEnd(Stack<Node> stack, Integer x){
        int size = stack.size();
        int k=0;
        while (!stack.isEmpty()) {
            Node element = stack.pop();
            if(element.getSecond() > x){
               k++;
            }
        }
        if(k==size) return true;
        else  return  false;
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