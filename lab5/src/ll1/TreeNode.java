package ll1;

import java.util.List;

public class TreeNode {
    private int index;
    private String info;
    private TreeNode parent;
    private List<TreeNode> children;

    public TreeNode(int index, String info, TreeNode parent, List<TreeNode> children) {
        this.index = index;
        this.info = info;
        this.parent = parent;
        this.children = children;
    }

    public int getIndex() {
        return index;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return String.format("%-7s  %-10s  %-2s",
                index, info, parent != null ? parent.getIndex() : 0) + '\n';
    }
}
