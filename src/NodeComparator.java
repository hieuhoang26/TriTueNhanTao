import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node node1, Node node2) {
        // So sánh Integer
        int integerComparison = Integer.compare(node1.getSecond(), node2.getSecond());
        if (integerComparison != 0) {
            return integerComparison;
        }
        // Nếu Integer bằng nhau, so sánh chuỗi
        return node1.getFirst().compareTo(node2.getFirst());
    }
}
