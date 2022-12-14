package grammar.parsing_table;

import java.util.List;

public class ParsingTableCell {
    private List<String> rhsOfProduction;
    private int numberOfProduction;

    public ParsingTableCell(List<String> rhsOfProduction, int numberOfProduction) {
        this.rhsOfProduction = rhsOfProduction;
        this.numberOfProduction = numberOfProduction;
    }
}
