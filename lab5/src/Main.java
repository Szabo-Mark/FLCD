import grammar.Grammar;
import grammar.Production;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar("resources/grammar.txt");
        showMenu(grammar);
    }

    private static void showMenu(Grammar grammar) {
        System.out.println("Hi!");
        while (true) {
            System.out.println("""
                                        
                    Press 1 to print the set of non terminals
                    Press 2 to print the set of terminals
                    Press 3 to print the set of productions
                    Press 4 to print productions for a given non terminal
                    Press 5 to do the CFG check
                    Press 6 to print follow function
                    Press 0 to exit
                                        
                    """);

            System.out.println("Your command:");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.strip().equals("1")) {
                System.out.println(grammar.getNonTerminals());
            } else if (command.strip().equals("2")) {
                System.out.println(grammar.getTerminals());
            } else if (command.strip().equals("3")) {
                System.out.println(grammar.getProductions());
            } else if (command.strip().equals("4")) {
                productionForAGivenNonTerminal(grammar);
            } else if (command.strip().equals("5")) {
                System.out.println("I dunno what that is");
            } else if (command.strip().equals("0")) {
                System.out.println("\nThanks for using the app! Goodbye");
                break;
            } else if (command.strip().equals("6")) {
                printFollowFunction(grammar);
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void productionForAGivenNonTerminal(Grammar grammar) {
        System.out.println("\tNon terminal:");
        Scanner scanner = new Scanner(System.in);
        String nonTerminal = scanner.nextLine().strip();
        if (!grammar.getNonTerminals().contains(nonTerminal)) {
            System.out.println("Invalid non terminal\n");
            return;
        }
        Set<Production> productions = grammar.getProductions();
        for (Production production : productions) {
            if (production.getRightSide().contains(nonTerminal)) {
                System.out.println(production);
            }
        }
    }

    private static void printFollowFunction(Grammar grammar) {
        var followFunction = grammar.getFollowFunction();
        for (Map.Entry<String, Set<String>> entry : followFunction.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
