import java.util.ArrayList;
import java.util.List;

public class Node<E> {
    private E data;
    List<Node<E>> child;
    public Node(E data) {
        this.data = data;
        this.child = new ArrayList<>();
    }
    public void addChild(E data){
        Node<E> newNode = new Node<>(data);
        this.child.add(newNode);
    }
    public Node() {
    }
    public E getData() {
        return data;
    }
    public void setData(E data) {
        this.data = data;
    }
    public List<Node<E>> getChild() {
        return child;
    }
    public void setChild(List<Node<E>> child) {
        this.child = child;
    }
    public boolean hasChild(){
        return this.child.size()!=0;
    }
}
