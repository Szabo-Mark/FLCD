package grammar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
        /*firstFunction.put("S", Set.of("(", "a"));
        firstFunction.put("A", Set.of("+", EPSILON));
        firstFunction.put("B", Set.of("(", "a"));
        firstFunction.put("C", Set.of("*", EPSILON));
        firstFunction.put("D", Set.of("(", "a"));
        firstFunction.put("a", Set.of("a"));
        firstFunction.put("+", Set.of("+"));
        firstFunction.put("*", Set.of("*"));
        firstFunction.put("(", Set.of("("));
        firstFunction.put(")", Set.of(")"));
        firstFunction.put(EPSILON, Set.of(EPSILON));*/
        firstForAll();
        computeFollowFunction();
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

    private void computeFollowFunction() {
        List<Iteration> iterations = initIterations();
        int i = 1;
        while (true) {
            Iteration previousIteration = iterations.get(i - 1);
            Iteration iteration = previousIteration.copy();
            for (String nonTerminal : nonTerminals) {
                List<Production> productions = getProductionsWithSymbolOnRHS(nonTerminal);
                for (Production production : productions) {
                    handleProduction(nonTerminal, production, previousIteration, iteration);
                }
            }
            if (iteration.equals(iterations.get(i - 1))) {
                followFunction = iteration.getFollowSets();
                break;
            }
            i++;
            iterations.add(iteration);
        }
    }

    private List<Iteration> initIterations() {
        List<Iteration> iterations = new ArrayList<>();
        Map<String, Set<String>> mapOfFirstIteration = new HashMap<>();
        nonTerminals.forEach((nonTerminal) -> {
            Set<String> followSet = new HashSet<>();
            mapOfFirstIteration.put(nonTerminal, followSet);
        });
        mapOfFirstIteration.get(startingSymbol).add(EPSILON);
        iterations.add(new Iteration(mapOfFirstIteration));
        return iterations;
    }

    private List<Production> getProductionsWithSymbolOnRHS(String symbol) {
        List<Production> productionsWithSymbolOnRHS = new ArrayList<>();
        for (Production production : productions) {
            if (production.getRightSide().contains(symbol)) {
                productionsWithSymbolOnRHS.add(production);
            }
        }
        return productionsWithSymbolOnRHS;
    }

    private void handleProduction(String nonTerminal, Production production, Iteration previousIteration, Iteration iteration) {
        List<String> rhs = production.getRightSide();
        int index = rhs.indexOf(nonTerminal);
        if (index == rhs.size() - 1) { // if it is the last one in the production
            Set<String> followSetOfLHS = previousIteration.getFollowSet(production.getLeftSide()); // Fi-1(A)
            iteration.getFollowSet(nonTerminal).addAll(followSetOfLHS);
        } else {
            String nextSymbol = rhs.get(index + 1);
            //Set<String> firstSetOfNextSymbol = firstFunction.get(nextSymbol);
            var firstSetOfNextSymbol = concatenationOfLengthOne(rhs.subList(index + 1, rhs.size()));
            if (firstSetOfNextSymbol.contains(EPSILON)) { // if it contains epsilon -> (FIRST(beta) - EPSILON) U FOLLOW(A)
                Set<String> copyOfFirstSetOfNextSymbol = new HashSet<>(firstSetOfNextSymbol);
                copyOfFirstSetOfNextSymbol.remove(EPSILON);
                iteration.getFollowSet(nonTerminal).addAll(copyOfFirstSetOfNextSymbol);
                Set<String> followSetOfLHS = previousIteration.getFollowSet(production.getLeftSide());
                iteration.getFollowSet(nonTerminal).addAll(followSetOfLHS);
            } else { // FIRST(beta)
                iteration.getFollowSet(nonTerminal).addAll(firstSetOfNextSymbol);
            }
        }
    }

    private Set<String> concatenationOfLengthOne(List<String> input) {
        Set<String> result = new HashSet<>();
        if (input.size() == 0) {
            result.add(EPSILON);
            return result;
        }
        Set<String> firsts = firstFunction.get(input.get(0));
        if (firsts.contains(EPSILON)) {
            result.addAll(concatenationOfLengthOne(input.subList(1, input.size())));
        }

        // flatten
        result.addAll(
                firsts.stream()
                        .filter(first -> !first.equals(EPSILON))
                        .map(first -> firstFunction.get(first))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet())
        );
        return result;
    }

    private Set<String> first(String item) {
        Set<String> firstItemSet = new HashSet<>();
        if (terminals.contains(item)) {
            firstItemSet.add(item);
            return firstItemSet;
        } else {
            List<Production> itemProductionSet = productionForAGivenNonTerminal(item);

            for (Production production : itemProductionSet) {
                if (production.getRightSide().contains("EPSILON")) {
                    firstItemSet.add("EPSILON");
                } else {
                    Set<String> provisionalFirstList;
                    int index = 0;
                    provisionalFirstList = first(production.getRightSide().get(index));
                    if (provisionalFirstList.contains("EPSILON")) {
                        while (provisionalFirstList.contains("EPSILON")) {
                            firstItemSet.addAll(provisionalFirstList);
                            index = index + 1;
                            provisionalFirstList = first(production.getRightSide().get(index));
                        }
                    } else {
                        firstItemSet.addAll(provisionalFirstList);
                    }
                }
            }
            firstFunction.put(item, firstItemSet);
        }
        return firstItemSet;
    }

    private List<Production> productionForAGivenNonTerminal(String nonTerminal) {
        List<Production> productionList = new ArrayList<>();

        for (Production production : productions) {
            if (production.getLeftSide().contains(nonTerminal)) {
                productionList.add(production);
            }
        }
        return productionList;
    }

    public void firstForAll() {
        terminals.forEach((terminal -> firstFunction.put(terminal, Set.of(terminal))));
        for (String nonTerminal : nonTerminals) {
            if (!firstFunction.containsKey(nonTerminal))
                first(nonTerminal);
        }
        System.out.println(firstFunction);
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

    public Map<String, Set<String>> getFollowFunction() {
        return followFunction;
    }
}