package scanner.symbolTable;

public class SymbolTable {
    private final HashTable hashTable;

    public SymbolTable() {
        this.hashTable = new HashTable();
    }

    public Position add(String token) {
        return hashTable.add(token);
    }

    @Override
    public String toString() {
        return hashTable.toString();
    }
}
