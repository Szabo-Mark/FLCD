package scanner.symbolTable;

public class Position {
    int positionInTable;
    int positionInList;

    public Position(int positionInTable, int positionInList) {
        this.positionInTable = positionInTable;
        this.positionInList = positionInList;
    }

    public int getPositionInTable() {
        return positionInTable;
    }

    public void setPositionInTable(int positionInTable) {
        this.positionInTable = positionInTable;
    }

    public int getPositionInList() {
        return positionInList;
    }

    public void setPositionInList(int positionInList) {
        this.positionInList = positionInList;
    }

    @Override
    public String toString() {
        return "(" + positionInTable + ", " + positionInList + ")";
    }
}
