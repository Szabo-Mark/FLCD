package scanner.programInternalFormTable;

import scanner.symbolTable.Position;
import scanner.utils.Pair;

import java.util.LinkedList;
import java.util.List;

public class ProgramInternalFormTable {
    private final List<Pair<String, Position>> list;

    public ProgramInternalFormTable() {
        list = new LinkedList<>();
    }

    public void addToList(String token, Position position) {
        list.add(new Pair<>(token, position));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(pair -> stringBuilder.append(pair.getFirstElem())
                .append(" ")
                .append(pair.getSecondElem())
                .append("\n"));
        return stringBuilder.toString();
    }
}
