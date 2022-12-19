import grammar.Grammar;
import ll1.FirstAndFollowCalculator;
import ll1.ParsingTable;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hi!");
        Grammar grammar = new Grammar("resources/grammar.txt");
        FirstAndFollowCalculator firstAndFollowCalculator = new FirstAndFollowCalculator(grammar);
        ParsingTable parsingTable = new ParsingTable(grammar, firstAndFollowCalculator);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
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
                default -> {
                    System.out.println("Bye");
                    running = false;
                }
            }
        }
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
                Press anything else and you exit.
                                
                """);
    }

    private static void productionForAGivenNonTerminal(Grammar grammar) {
        System.out.println("\tNon terminal:");
        Scanner scanner = new Scanner(System.in);
        String nonTerminal = scanner.nextLine().strip();
        grammar.getProductionForNonTerminal(nonTerminal)
                .ifPresent(System.out::println);
    }
}
