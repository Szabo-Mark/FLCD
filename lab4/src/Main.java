import FiniteAutomata.FiniteAutomata;

import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        FiniteAutomata finiteAutomata = new FiniteAutomata("resources/FA.in");
        printMenu();
        Scanner scanner = new Scanner(System.in);
        int option;
        boolean running = true;
        while (running) {
            System.out.print("Choose your option : ");
            option = scanner.nextInt();
            switch (option) {
                case 1 -> printStates(finiteAutomata);
                case 2 -> printSymbols(finiteAutomata);
                case 3 -> printInitialState(finiteAutomata);
                case 4 -> printFinalStates(finiteAutomata);
                case 5 -> printTransitionFunction(finiteAutomata);
                case 6 -> checkSequence(finiteAutomata);
                default -> running = false;
            }
        }
        System.out.println("Bye");
    }

    private static void checkSequence(FiniteAutomata finiteAutomata) {
        if (!finiteAutomata.isDFA()) {
            System.out.println("Not DFA!!!!");
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("sequence: ");
            String sequence = scanner.nextLine();
            System.out.println(finiteAutomata.accepts(sequence));
        }
    }

    private static void printMenu() {
        Stream.of("0- Exit",
                        "1- States",
                        "2- Symbols",
                        "3- Initial State",
                        "4- Final States",
                        "5- Transition Function",
                        "6 - Check sequence")
                .forEach(System.out::println);
    }

    private static void printStates(FiniteAutomata finiteAutomata) {
        StringBuilder stringBuilder = new StringBuilder("STATES\n");
        finiteAutomata.getStates().forEach(state -> stringBuilder.append(state).append('\n'));
        System.out.println(stringBuilder);
    }

    private static void printSymbols(FiniteAutomata finiteAutomata) {
        StringBuilder stringBuilder = new StringBuilder("SYMBOLS\n");
        finiteAutomata.getSymbols().forEach(state -> stringBuilder.append(state).append('\n'));
        System.out.println(stringBuilder);
    }

    private static void printInitialState(FiniteAutomata finiteAutomata) {
        System.out.println("INITIAL_STATE\n" + finiteAutomata.getInitialState() + '\n');
    }

    private static void printFinalStates(FiniteAutomata finiteAutomata) {
        StringBuilder stringBuilder = new StringBuilder("FINAL_STATES\n");
        finiteAutomata.getFinalStates().forEach(state -> stringBuilder.append(state).append('\n'));
        System.out.println(stringBuilder);
    }

    private static void printTransitionFunction(FiniteAutomata finiteAutomata) {
        System.out.println(finiteAutomata.getTransitionFunction());
    }
}
