package grammar.parsing_table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParsingConfiguration {

    private ParsingTable parsingTable;
    private List<String> inputStack;
    private List<String> workingStack;
    private List<String> outputStack;

    public ParsingConfiguration(ParsingTable parsingTable, List<String> inputStack, List<String> workingStack, List<String> outputStack) {
        this.parsingTable = parsingTable;
        this.inputStack = inputStack;
        this.workingStack = workingStack;
        this.outputStack = outputStack;
    }

    @Override
    public String toString() {
        return "ParsingConfiguration{" +
                "parsingTable=" + parsingTable +
                ", inputStack=" + inputStack +
                ", workingStack=" + workingStack +
                ", outputStack=" + outputStack +
                '}';
    }

    public ParsingTable getParsingTable() {
        return parsingTable;
    }

    public List<String> getInputStack() {
        return inputStack;
    }

    public List<String> getWorkingStack() {
        return workingStack;
    }

    public List<String> getOutputStack() {
        return outputStack;
    }

    public void push() {
        List<String> cellResult = parsingTable.get(workingStack.get(0), inputStack.get(0)).getRhsOfProduction();
        workingStack.remove(0);
        List<String> newWorkingStack = new ArrayList<>();
        newWorkingStack.addAll(cellResult);
        newWorkingStack.addAll(workingStack);
        workingStack = newWorkingStack;
        outputStack.add(String.valueOf(parsingTable.get(workingStack.get(0), inputStack.get(0)).getNumberOfProduction()));
    }

    public void pop() {
        workingStack.remove(0);
        inputStack.remove(0);
    }

    public Boolean accept() {
        if (inputStack.size() == 1 && Objects.equals(inputStack.get(0), "$") && workingStack.size() == 1 && Objects.equals(workingStack.get(0), "$") && outputStack.size() == 1 && Objects.equals(outputStack.get(0), "pi")) {
            System.out.println("acc");
            return true;
        } else {
            return false;
        }
    }
}
