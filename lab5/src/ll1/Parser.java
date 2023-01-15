package ll1;

import grammar.Grammar;
import grammar.Production;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static ll1.Constants.END_SYMBOL;

public class Parser {
    private final static String OUTPUT_FILE_PATH = "resources/output.txt";

    private ParsingTable parsingTable;
    private Grammar grammar;

    public Parser(ParsingTable parsingTable, Grammar grammar) {
        this.parsingTable = parsingTable;
        this.grammar = grammar;
    }

    public List<Integer> getSequenceOfProductions(List<String> sequence) {
        List<String> inputStack = new ArrayList<>(sequence);
        inputStack.add(END_SYMBOL);
        List<String> workingStack = new ArrayList<>();
        workingStack.add(grammar.getStartingSymbol());
        workingStack.add(END_SYMBOL);
        List<Integer> outputStack = new ArrayList<>();
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
                        System.out.println("ParsingTable error at: " + configuration.getHeadOfWorkingStack() + ',' + configuration.getHeadOfInputStack());
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

    public List<TreeNode> getTree(List<String> sequence) {
        List<Integer> sequenceOfProductions = getSequenceOfProductions(sequence);
        return convertSequenceOfProductionsIntoTree(sequenceOfProductions);
    }

    public List<TreeNode> convertSequenceOfProductionsIntoTree(List<Integer> sequenceOfProductions) {
        List<TreeNode> treeMatrix = new ArrayList<>();
        treeMatrix.add(new TreeNode(1, grammar.getStartingSymbol(), null, new LinkedList<>()));
        for (Integer indexOfProduction : sequenceOfProductions) {
            TreeNode parentNode = getLeftMostNonTerminalNodeFromTree(treeMatrix.get(0));
            Production production = grammar.getProductionWithIndex(indexOfProduction);
            List<String> productionRightSide = production.getRightSide();
            int index = treeMatrix.size() + 1; //new nodes will start with this index
            for (int childCount = 0; childCount < productionRightSide.size(); childCount++) {
                TreeNode newChild;
                String info = productionRightSide.get(childCount);
                if (grammar.getNonTerminals().contains(info)) {
                    //if it is a non-terminal we'll put an empty list for children
                    newChild = new TreeNode(index + childCount, productionRightSide.get(childCount), parentNode,
                            new LinkedList<>());
                } else {
                    //if it is a terminal we put null.
                    newChild = new TreeNode(index + childCount, productionRightSide.get(childCount), parentNode, null);
                }
                treeMatrix.add(newChild);
                parentNode.getChildren().add(newChild);
            }
        }
        writeToFile(treeMatrix);
        return treeMatrix;
    }

    private boolean isSpecialCase(ParsingTableCell parsingTableCell) {
        return Objects.equals(parsingTableCell, ParsingTableCell.getAcceptCell())
                || Objects.equals(parsingTableCell, ParsingTableCell.getPopCell())
                || Objects.equals(parsingTableCell, ParsingTableCell.getErrorCell());
    }

    private TreeNode getLeftMostNonTerminalNodeFromTree(TreeNode currentNode) {
        if (currentNode != null && currentNode.getChildren() != null) { //this is dfs basically
            if (currentNode.getChildren().isEmpty()) { //if leaf but non-terminal we found it.
                return currentNode;
            } else {
                for (TreeNode childNode : currentNode.getChildren()) {
                    //we check for each child starting from left to right.
                    TreeNode node = getLeftMostNonTerminalNodeFromTree(childNode);
                    if (node.getChildren() != null && node.getChildren().isEmpty()) {
                        return node; //probably im repeating myself here, but it's too late for me to think
                        // better
                    }
                }
            }
        }
        //if the children list is null -> leaf with a terminal -> we skip.
        return null;
    }

    private void writeToFile(List<TreeNode> matrix) {
        try {
            String head = "Index\tInfo\tParent node" + '\n';
            FileWriter fileWriter = new FileWriter(OUTPUT_FILE_PATH);
            fileWriter.write(head);
            for (TreeNode treeNode : matrix) {
                fileWriter.write(treeNode.toString() + "\n");
            }
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
