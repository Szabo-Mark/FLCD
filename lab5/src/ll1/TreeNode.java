package ll1;

public class TreeNode {
    private int index;
    private String info;
    private int parent;
    private int sibling;

    public TreeNode(int index, String info, int parent, int sibling) {
        this.index = index;
        this.info = info;
        this.parent = parent;
        this.sibling = sibling;
    }

    public int getIndex() {
        return index;
    }

    public String getInfo() {
        return info;
    }

    public int getParent() {
        return parent;
    }

    public int getSibling() {
        return sibling;
    }
}
