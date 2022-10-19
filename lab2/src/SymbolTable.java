public class SymbolTable {
    private HashTable hashTable;

    public Position add(String token) {
        return hashTable.add(token);
    }
}
