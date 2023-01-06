package ll1;

import java.util.List;
import java.util.Objects;

import static ll1.Constants.ACCEPT;
import static ll1.Constants.ERROR;
import static ll1.Constants.POP;

public class ParsingTableCell {
    private List<String> rhsOfProduction;
    private int numberOfProduction;

    public ParsingTableCell(List<String> rhsOfProduction, int numberOfProduction) {
        this.rhsOfProduction = rhsOfProduction;
        this.numberOfProduction = numberOfProduction;
    }

    public static ParsingTableCell getErrorCell() {
        return new ParsingTableCell(List.of(ERROR), -1);
    }

    public static ParsingTableCell getAcceptCell() {
        return new ParsingTableCell(List.of(ACCEPT), -1);
    }

    public static ParsingTableCell getPopCell() {
        return new ParsingTableCell(List.of(POP), -1);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsingTableCell that = (ParsingTableCell) o;
        return numberOfProduction == that.numberOfProduction && Objects.equals(rhsOfProduction, that.rhsOfProduction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rhsOfProduction, numberOfProduction);
    }

    @Override
    public String toString() {
        return "(" + rhsOfProduction + ", " + numberOfProduction + ")";
    }
}
