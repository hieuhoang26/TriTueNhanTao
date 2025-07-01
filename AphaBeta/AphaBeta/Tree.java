public class Tree<E> {
    private Node<E> root;

    public Tree() {
        this.root = null;
    }

    public Node<E> getRoot() {
        return root;
    }

    public void setRoot(Node<E> root) {
        this.root = root;
    }
    
    public void printPreorder(Node<E> node)
	{
		if (node == null)
		{
			return;
		}
		int i = 0;
		Node<E> temp = null;
        System.out.print(node.getData() + " ");
		while (i < node.getChild().size())
		{
			temp = node.getChild().get(i);
			this.printPreorder(temp);
			i++;
		}
	}

}
