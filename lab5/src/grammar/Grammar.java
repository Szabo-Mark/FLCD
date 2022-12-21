package grammar;

import ll1.ParsingTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static utils.Constants.EPSILON;

public class Grammar {
    private final Set<String> nonTerminals;
    private final Set<String> terminals;
    private final List<Production> productions;
    private String startingSymbol;
    private final Map<Production, Integer> productionIndex;

    public Grammar(String pathToFile) {
        nonTerminals = new HashSet<>();
        terminals = new HashSet<>();
        productions = new ArrayList<>();
        startingSymbol = null;
        readFromFile(pathToFile);
        productionIndex = initProductionIndex();
    }

    private Map<Production, Integer> initProductionIndex() {
        Map<Production, Integer> mapProductionToIndex = new HashMap<>();
        int index = 1;
        for (Production production : productions) {
            mapProductionToIndex.put(production, index);
            index++;
        }
        return mapProductionToIndex;
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
                if (!nonTerminals.contains(tkn) && !terminals.contains(tkn) && !Objects.equals(tkn, EPSILON)) {
                    return Optional.of("Token " + tkn + " is neither a non terminal nor a terminal!");
                }
                rightSideList.add(tkn);
            }
            productions.add(new Production(leftSide, rightSideList));
        }
        return Optional.empty();
    }

    public Optional<Production> getProductionForNonTerminal(String nonTerminal){
        return productions.stream()
                .filter(production -> production.getRightSide().contains(nonTerminal))
                .findFirst();
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


    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    public Map<Production, Integer> getProductionIndex() {
        return productionIndex;
    }
}