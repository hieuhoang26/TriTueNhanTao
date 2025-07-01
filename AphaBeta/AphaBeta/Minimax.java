class Minimax {

    public static String Max(String a, String b) {
        try {
            int valA = Integer.parseInt(a);
            int valB = Integer.parseInt(b);
            return valA > valB ? a : b;
        } catch (NumberFormatException e) {
            System.out.println("Error");
            return null;
        }
    }
    public static String Min(String a, String b) {
        try {
            int valA = Integer.parseInt(a);
            int valB = Integer.parseInt(b);
            return valA < valB ? a : b;
        } catch (NumberFormatException e) {
            System.out.println("Error");
            return null;
        }
    }
    public static int equal(String a, String b){
        try {
            int valA = Integer.parseInt(a);
            int valB = Integer.parseInt(b);
            if(valA >= valB){
                return 1;
            }
            if(valA <= valB){
                return 0;
            }
            return -1;
        } catch (NumberFormatException e) {
            System.out.println("Error");
            return -1;
        }
    }

    public static String minimax(Node<String> root, boolean isMax, String alpha, String beta) {
        if (!root.hasChild()) {
            return root.getData();
        }
        if (isMax) {
            String v = "-9999";
            for(Node<String> child: root.getChild()){
                String val = minimax(child, false, alpha, beta);
                v = Max(v, val);
                alpha = Max(alpha, v);
                if(equal(val, beta) == 1){
                    System.out.println(root.getData() + " " + child.getData());
                    break;
                }
            }
            return v;
        }
        else{
            String v = "9999";
            for(Node<String> child: root.getChild()){
                String val = minimax(child, true, alpha, beta);
                v = Min(v, val);
                beta = Min(beta, v);
                if(equal(alpha, val) == 1){
                    System.out.println(root.getData() + " " + child.getData());
                    break;
                }
            }
            return v;
        }
    }

    public static void main(String[] args) {
        Tree<String> tree = new Tree<>();
        tree.setRoot(new Node<String>("A"));
        tree.getRoot().addChild("B");
        tree.getRoot().addChild("C");
        tree.getRoot().addChild("D");
        // node B
        tree.getRoot().getChild().get(0).addChild("E");
        tree.getRoot().getChild().get(0).addChild("F");
        // node E
        tree.getRoot().getChild().get(0).getChild().get(0).addChild("2");
        tree.getRoot().getChild().get(0).getChild().get(0).addChild("6");
        // node F
        tree.getRoot().getChild().get(0).getChild().get(1).addChild("5");
        tree.getRoot().getChild().get(0).getChild().get(1).addChild("6");
        tree.getRoot().getChild().get(0).getChild().get(1).addChild("3");
        // node C
        tree.getRoot().getChild().get(1).addChild("7");
        tree.getRoot().getChild().get(1).addChild("8");
        tree.getRoot().getChild().get(1).addChild("G");
        // node G
        tree.getRoot().getChild().get(1).getChild().get(2).addChild("9");
        tree.getRoot().getChild().get(1).getChild().get(2).addChild("1");
        tree.getRoot().getChild().get(1).getChild().get(2).addChild("3");
        // node D
        tree.getRoot().getChild().get(2).addChild("H");
        tree.getRoot().getChild().get(2).addChild("I");
        tree.getRoot().getChild().get(2).addChild("J");
        // node H
        tree.getRoot().getChild().get(2).getChild().get(0).addChild("5");
        tree.getRoot().getChild().get(2).getChild().get(0).addChild("2");
        tree.getRoot().getChild().get(2).getChild().get(0).addChild("4");
        // node I
        tree.getRoot().getChild().get(2).getChild().get(1).addChild("7");
        // node J
        tree.getRoot().getChild().get(2).getChild().get(2).addChild("9");
        tree.getRoot().getChild().get(2).getChild().get(2).addChild("4");
        tree.printPreorder(tree.getRoot());
        System.out.printf("Minimax: %s", minimax(tree.getRoot(), true, "-9999", "9999"));
    }
}