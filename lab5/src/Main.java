import grammar.Grammar;
import ll1.FirstAndFollowCalculator;
import ll1.Parser;
import ll1.ParsingTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final static String PATH_TO_G1 = "resources/g1.txt";
    private final static String PATH_TO_SEQ = "resources/seq.txt";

    public static void main(String[] args) throws IOException {
        System.out.println("Hi!");
        Grammar grammar = new Grammar(PATH_TO_G1);
        FirstAndFollowCalculator firstAndFollowCalculator = new FirstAndFollowCalculator(grammar);
        ParsingTable parsingTable = new ParsingTable(grammar, firstAndFollowCalculator);
        Parser parser = new Parser(parsingTable, grammar);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        printMenu();
        int option;
        while (running) {
            option = scanner.nextInt();
            switch (option) {
                case 0 -> printMenu();
                case 1 -> System.out.println(grammar.getNonTerminals());
                case 2 -> System.out.println(grammar.getTerminals());
                case 3 -> System.out.println(grammar.getProductions());
                case 4 -> productionForAGivenNonTerminal(grammar);
                case 5 -> System.out.println(firstAndFollowCalculator.getFirstFunctionString());
                case 6 -> System.out.println(firstAndFollowCalculator.getFollowFunctionString());
                case 7 -> System.out.println(parsingTable);
                case 8 -> checkSequence(parser, PATH_TO_SEQ);
                default -> {
                    System.out.println("Bye");
                    running = false;
                }
            }
        }
    }

    private static void checkSequence(Parser parser, String pathToSequence) throws IOException {
        List<String> sequence = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToSequence));
        String line = bufferedReader.readLine();
        while (line != null) {
            sequence.add(line);
            line = bufferedReader.readLine();
        }
        List<Integer> sequenceOfProductions = parser.getSequenceOfProductions(sequence);
        System.out.println(sequenceOfProductions);
        System.out.println(parser.convertSequenceOfProductionsIntoTree(sequenceOfProductions));
    }


    private static void productionForAGivenNonTerminal(Grammar grammar) {
        System.out.println("\tNon terminal:");
        Scanner scanner = new Scanner(System.in);
        String nonTerminal = scanner.nextLine().strip();
        grammar.getProductionForNonTerminal(nonTerminal)
                .ifPresent(System.out::println);
    }

    private static void printMenu() {
        System.out.println("""
                                  
                Press 1 to print the set of non terminals
                Press 2 to print the set of terminals
                Press 3 to print the set of productions
                Press 4 to print productions for a given non terminal
                Press 5 to print first sets
                Press 6 to print follow sets
                Press 7 to print parsing table
                Press 8 to check if g1.txt sequence works
                Press anything else and you exit.
                                
                """);
    }
}
