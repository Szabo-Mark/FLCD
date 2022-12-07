package grammar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Grammar {
    private static final String EPSILON = "EPSILON";
    private final Set<String> nonTerminals;
    private final Set<String> terminals;
    private final Set<Production> productions;
    private String startingSymbol;
    private Map<String, Set<String>> followFunction;
    private Map<String, Set<String>> firstFunction;

    public Grammar(String pathToFile) {
        nonTerminals = new HashSet<>();
        terminals = new HashSet<>();
        terminals.add(EPSILON);
        productions = new HashSet<>();
        startingSymbol = null;
        followFunction = new HashMap<>();
        firstFunction = new HashMap<>();
        readFromFile(pathToFile);
    }

    private void readFromFile(String pathToFile) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToFile))) {
            StringBuilder errorStringBuilder = new StringBuilder();
            GrammarFileDelimiter lastFileDelimiter = null;
            GrammarFileDelimiter faFileDelimiter;
            String line = bufferedReader.readLine();
            int lineCount = 1;
            while (line != null) {
                faFileDelimiter = checkIfDelimiter(line);
                if (faFileDelimiter != null) {
                    lastFileDelimiter = faFileDelimiter;
                } else {
                    Optional<String> error = handleLine(lastFileDelimiter, line.strip());
                    if (error.isPresent()) {
                        errorStringBuilder.append("At line: ")
                                .append(lineCount)
                                .append(" -> ")
                                .append(error.get())
                                .append('\n');
                    }
                }
                line = bufferedReader.readLine();
                lineCount++;
            }
            if (errorStringBuilder.isEmpty()) {
                System.out.println("Grammar read successfully from " + pathToFile);
            } else {
                System.out.println("Errors\n" + errorStringBuilder);
            }

        } catch (IOException exception) {
            System.out.println("File could not be parsed!");
        }
    }

    private Optional<String> handleLine(GrammarFileDelimiter fileDelimiter, String line) {
        return switch (fileDelimiter) {
            case NON_TERMINALS -> handleNonTerminals(line);
            case TERMINALS -> handleTerminals(line);
            case STARTING_SYMBOL -> handleStartingSymbol(line);
            case PRODUCTIONS -> handleProductions(line);
        };
    }

    private Optional<String> handleProductions(String line) {
        String[] sides = line.split(":");
        if (sides.length != 2) {
            return Optional.of("Wrong format for production " + line);
        }
        String leftSide = sides[0];
        if (!nonTerminals.contains(leftSide.strip())) {
            return Optional.of("Non terminal " + leftSide + " was not defined!");
        }
        String rightSide = sides[1];
        return handleRightSideOfProduction(leftSide, rightSide);
    }

    private Optional<String> handleRightSideOfProduction(String leftSide, String rightSide) {
        String[] rightSideTokens = rightSide.split("\\|");
        for (String token : rightSideTokens) {
            String[] splitAfterSpace = token.split(" ");
            List<String> rightSideList = new ArrayList<>();
            for (String tkn : splitAfterSpace) {
                tkn = tkn.strip();
                if (!nonTerminals.contains(tkn) && !terminals.contains(tkn)) {
                    return Optional.of("Token " + tkn + " is neither a non terminal nor a terminal!");
                }
                rightSideList.add(tkn);
            }
            productions.add(new Production(leftSide, rightSideList));
        }
        return Optional.empty();
    }

    private Optional<String> handleStartingSymbol(String line) {
        if (startingSymbol != null) {
            return Optional.of("Cannot have more than one starting symbol");
        }
        if (!nonTerminals.contains(line)) {
            return Optional.of("Starting symbol " + line + "is not defined!");
        }
        startingSymbol = line;
        return Optional.empty();
    }

    private Optional<String> handleTerminals(String line) {
        if (!terminals.add(line))
            return Optional.of("Terminal" + line + "is already defined!");
        return Optional.empty();
    }

    private Optional<String> handleNonTerminals(String line) {
        if (!nonTerminals.add(line))
            return Optional.of("Non terminal" + line + "is already defined!");
        return Optional.empty();
    }

    private GrammarFileDelimiter checkIfDelimiter(String line) {
        if (Objects.equals(line, GrammarFileDelimiter.NON_TERMINALS.toString())) {
            return GrammarFileDelimiter.NON_TERMINALS;
        }
        if (Objects.equals(line, GrammarFileDelimiter.TERMINALS.toString())) {
            return GrammarFileDelimiter.TERMINALS;
        }
        if (Objects.equals(line, GrammarFileDelimiter.STARTING_SYMBOL.toString())) {
            return GrammarFileDelimiter.STARTING_SYMBOL;
        }
        if (Objects.equals(line, GrammarFileDelimiter.PRODUCTIONS.toString())) {
            return GrammarFileDelimiter.PRODUCTIONS;
        }
        return null;
    }

    public void showMenu() {
        System.out.println("Hi!");
        while (true) {
            System.out.println("""
                    
                    Press 1 to print the set of non terminals
                    Press 2 to print the set of terminals
                    Press 3 to print the set of productions
                    Press 4 to print productions for a given non terminal
                    Press 5 to do the CFG check
                    Press 0 to exit
                    
                    """);

            System.out.println("Your command:");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.strip().equals("1")) {
                System.out.println(getNonTerminals());
            } else if (command.strip().equals("2")) {
                System.out.println(getTerminals());
            } else if (command.strip().equals("3")) {
                System.out.println(getProductions());
            } else if (command.strip().equals("4")) {
                productionForAGivenNonTerminal();
            } else if (command.strip().equals("5")) {
                System.out.println("I dunno what that is");
            } else if (command.strip().equals("0")) {
                System.out.println("\nThanks for using the app! Goodbye");
                break;
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public Set<Production> getProductions() {
        return productions;
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    private void productionForAGivenNonTerminal() {
        System.out.println("\tNon terminal:");
        Scanner scanner = new Scanner(System.in);
        String nonTerminal = scanner.nextLine().strip();
        if (!nonTerminals.contains(nonTerminal)) {
            System.out.println("Invalid non terminal\n");
            return;
        }

        for (Production production : productions) {
            if (production.getRightSide().contains(nonTerminal)) {
                System.out.println(production);
            }
        }
    }
}