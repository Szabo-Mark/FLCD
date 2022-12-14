package grammar.parsing_table;

import java.util.ArrayList;
import java.util.List;

public class ParsingTableCell {
    private List<String> rhsOfProduction;
    private int numberOfProduction;

    public ParsingTableCell() {
        rhsOfProduction = new ArrayList<>();
        numberOfProduction = 0;
    }

    public ParsingTableCell(List<String> rhsOfProduction, int numberOfProduction) {
        this.rhsOfProduction = rhsOfProduction;
        this.numberOfProduction = numberOfProduction;
    }

    public static ParsingTableCell getErrorCell() {
        return new ParsingTableCell(List.of("ERROR"), 0);
    }

    public static ParsingTableCell getAcceptCell(){
        return new ParsingTableCell(List.of("ACCEPT"), 0);
    }

    public static ParsingTableCell getPopCell(){
        return new ParsingTableCell(List.of("POP"), 0);
    }

    public List<String> getRhsOfProduction() {
        return rhsOfProduction;
    }

    public void setRhsOfProduction(List<String> rhsOfProduction) {
        this.rhsOfProduction = rhsOfProduction;
    }

    public int getNumberOfProduction() {
        return numberOfProduction;
    }

    public void setNumberOfProduction(int numberOfProduction) {
        this.numberOfProduction = numberOfProduction;
    }
}
