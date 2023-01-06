package ll1;

import grammar.Grammar;
import grammar.Production;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parser {
    private ParsingTable parsingTable;
    private Grammar grammar;

    public Parser(ParsingTable parsingTable, Grammar grammar) {
        this.parsingTable = parsingTable;
        this.grammar = grammar;
    }

    public List<String> getSequenceOfProductions(List<String> sequence) {
        List<String> inputStack = new ArrayList<>(sequence);
        inputStack.add("$");
        List<String> workingStack = new ArrayList<>();
        workingStack.add(grammar.getStartingSymbol());
        workingStack.add("$");
        List<String> outputStack = new ArrayList<>();
        Configuration configuration = new Configuration(parsingTable, inputStack, workingStack, outputStack);

        boolean accepted = false;
        boolean go = true;
        while (go) {
            ParsingTableCell parsingTableCell =
                    parsingTable.get(configuration.getHeadOfWorkingStack(), configuration.getHeadOfInputStack());
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
        if (accepted) {
            return configuration.getOutputStack();
        }
        return null;
    }

    public List<TreeNode> getTree(List<String> sequence){
        List<String> sequenceOfProductions = getSequenceOfProductions(sequence);
        return convertSequenceOfProductionsIntoTree(sequenceOfProductions);
    }

    private boolean isSpecialCase(ParsingTableCell parsingTableCell) {
        return Objects.equals(parsingTableCell, ParsingTableCell.getAcceptCell())
                || Objects.equals(parsingTableCell, ParsingTableCell.getPopCell())
                || Objects.equals(parsingTableCell, ParsingTableCell.getErrorCell());
    }

    private List<TreeNode> convertSequenceOfProductionsIntoTree(List<Integer> sequenceOfProductions) {
        List<TreeNode> treeMatrix = new ArrayList<>();
        treeMatrix.add(new TreeNode(1, grammar.getStartingSymbol(), 0, 0));
        for (Integer indexOfProduction : sequenceOfProductions) {
            TreeNode parentNode = getLeftMostNonTerminalNodeFromTree(treeMatrix);
            Production production = grammar.getProductionWithIndex(indexOfProduction);
            List<String> productionRightSide = production.getRightSide();
            int index = treeMatrix.size(); //new nodes will start with this index
            for (int siblingCount = 0; siblingCount < productionRightSide.size(); siblingCount++) {
                treeMatrix.add(new TreeNode(index, productionRightSide.get(siblingCount), parentNode.getIndex(),
                        siblingCount));
            }
        }
        return treeMatrix;
    }

    private TreeNode getLeftMostNonTerminalNodeFromTree(List<TreeNode> treeMatrix) {
        return treeMatrix.stream()
                .filter(treeNode -> grammar.getNonTerminals().contains(treeNode.getInfo()))
                .findFirst()
                .orElseThrow();
    }
}
