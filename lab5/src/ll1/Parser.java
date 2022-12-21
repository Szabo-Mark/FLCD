package ll1;

import grammar.Grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static utils.Constants.EPSILON;

public class Parser {
    private ParsingTable parsingTable;
    private Grammar grammar;

    public Parser(ParsingTable parsingTable, Grammar grammar) {
        this.parsingTable = parsingTable;
        this.grammar = grammar;
    }

    public List<String> parseSequence(List<String> sequence) {

        List<String> inputStack = new ArrayList<>(sequence);
        inputStack.add("$");
        List<String> workingStack = new ArrayList<>();
        workingStack.add(grammar.getStartingSymbol());
        workingStack.add("$");
        List<String> outputStack = new ArrayList<>();
        outputStack.add(EPSILON);
        Configuration configuration = new Configuration(parsingTable, inputStack, workingStack, outputStack);

        boolean accepted = false;
        boolean go = true;
        while (go) {
            ParsingTableCell parsingTableCell = parsingTable.get(configuration.getHeadOfWorkingStack(), configuration.getHeadOfInputStack());
            if (!isSpecialCase(parsingTableCell)) {
                configuration.push();
            } else {
                if (Objects.equals(parsingTableCell, ParsingTableCell.getPopCell())) {
                    configuration.pop();
                } else {
                    if (Objects.equals(parsingTableCell, ParsingTableCell.getAcceptCell())) {
                        go = false;
                        accepted = true;
                    } else {
                        go = false;
                    }
                }
            }
        }
        if(accepted){
            return configuration.getOutputStack();
        }
        return null;
    }

    private boolean isSpecialCase(ParsingTableCell parsingTableCell) {
        return Objects.equals(parsingTableCell, ParsingTableCell.getAcceptCell())
                || Objects.equals(parsingTableCell, ParsingTableCell.getPopCell())
                || Objects.equals(parsingTableCell, ParsingTableCell.getErrorCell());
    }
}
