import java.util.Objects;

public class Node{
    private String first;
    private Integer second;

    public Node() {
    }

    public Node(String first, Integer second) {
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public void readNode (String string ){
        String[] x = string.split("-");
        this.first = x[0];
        this.second = Integer.parseInt(x[1]);
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
        if (!(o instanceof Node node)) return false;
        return Objects.equals(getFirst(), node.getFirst()) && Objects.equals(getSecond(), node.getSecond());
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}