import java.util.Objects;

public class Node<T,U>{
    private T first;
    private U second;

    public Node() {
    }

    public Node(T first, U second) {
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
        Node<?, ?> point = (Node<?, ?>) o;
        return Objects.equals(first, point.first) &&
                Objects.equals(second, point.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}