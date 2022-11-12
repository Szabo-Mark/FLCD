package scanner.symbolTable;

public class HashTable {
    private final Node[] table;

    public HashTable() {
        this.table = new Node[100];
    }

    public Position add(String token) {
        int key = hash(token) % table.length;
        Node headNode = table[key];
        if (headNode == null) {
            table[key] = getNewNode(token);
            return new Position(key, 0);
        }
        if (headNode.getValue().equals(token)) {
            return new Position(key, 0);
        }
        int positionInLinkedList = 1;
        Node nextNode = headNode.getNext();
        while (nextNode != null) {
            if (nextNode.getValue().equals(token))
                return new Position(key, positionInLinkedList);
            headNode = nextNode;
            nextNode = nextNode.getNext();
            positionInLinkedList++;
        }
        headNode.setNext(getNewNode(token));
        return new Position(key, positionInLinkedList);
    }

    private int hash(String string) {
        int hash = 7;
        char[] chars = string.toCharArray();
        for (char character : chars) {
            hash = hash * 31 + character;
        }
        return hash;
    }

    private Node getNewNode(String token) {
        Node newNode = new Node();
        newNode.setValue(token);
        newNode.setNext(null);
        return newNode;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : table) {
            while (node != null) {
                stringBuilder.append(node)
                        .append(" ")
                        .append(add(node.getValue()))
                        .append("\n");
                node = node.getNext();
            }
        }
        return stringBuilder.toString();
    }
}
