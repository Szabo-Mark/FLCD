package ll1;

import grammar.Grammar;
import grammar.Production;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static utils.Constants.EPSILON;

public class FirstAndFollowCalculator {
    private final Grammar grammar;
    private Map<String, Set<String>> followFunction;
    private final Map<String, Set<String>> firstFunction;

    public FirstAndFollowCalculator(Grammar grammar) {
        this.grammar = grammar;
        firstFunction = new HashMap<>();
        followFunction = new HashMap<>();
        computeFirstFunction();
        computeFollowFunction();
    }

    public static Set<String> concatenationOfLengthOne(List<String> input, Map<String, Set<String>> firstFunction) {
        Set<String> result = new HashSet<>();
        if (input.size() == 0) {
            result.add(EPSILON);
            return result;
        }
        Set<String> firsts = firstFunction.get(input.get(0));
        if (firsts.contains(EPSILON)) {
            result.addAll(concatenationOfLengthOne(input.subList(1, input.size()), firstFunction));
        }
        result.addAll(
                firsts.stream()
                        .filter(first -> !first.equals(EPSILON))
                        .map(firstFunction::get)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet())
        );
        return result;
    }

    private void computeFollowFunction() {
        List<Iteration> iterations = initIterations();
        int i = 1;
        while (true) {
            Iteration previousIteration = iterations.get(i - 1);
            Iteration iteration = previousIteration.copy();
            for (String nonTerminal : grammar.getNonTerminals()) {
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
        grammar.getNonTerminals().forEach((nonTerminal) -> {
            Set<String> followSet = new HashSet<>();
            mapOfFirstIteration.put(nonTerminal, followSet);
        });
        mapOfFirstIteration.get(grammar.getStartingSymbol()).add(EPSILON);
        iterations.add(new Iteration(mapOfFirstIteration));
        return iterations;
    }

    private List<Production> getProductionsWithSymbolOnRHS(String symbol) {
        List<Production> productionsWithSymbolOnRHS = new ArrayList<>();
        for (Production production : grammar.getProductions()) {
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
            var firstSetOfNextSymbol = concatenationOfLengthOne(rhs.subList(index + 1, rhs.size()), firstFunction);
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

    private void computeFirstFunction() {
        grammar.getTerminals().add(EPSILON);
        grammar.getTerminals().forEach((terminal -> firstFunction.put(terminal, Set.of(terminal))));
        for (String nonTerminal : grammar.getNonTerminals()) {
            if (!firstFunction.containsKey(nonTerminal))
                first(nonTerminal);
        }
        grammar.getTerminals().remove(EPSILON);
    }

    private Set<String> first(String item) {
        Set<String> firstItemSet = new HashSet<>();
        if (grammar.getTerminals().contains(item)) {
            firstItemSet.add(item);
            return firstItemSet;
        } else {
            List<Production> itemProductionSet = productionForAGivenNonTerminal(item);

            for (Production production : itemProductionSet) {
                if (production.getRightSide().contains(EPSILON)) {
                    firstItemSet.add(EPSILON);
                } else {
                    Set<String> provisionalFirstList;
                    int index = 0;
                    provisionalFirstList = first(production.getRightSide().get(index));
                    if (provisionalFirstList.contains(EPSILON)) {
                        while (provisionalFirstList.contains(EPSILON)) {
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

        for (Production production : grammar.getProductions()) {
            if (production.getLeftSide().contains(nonTerminal)) {
                productionList.add(production);
            }
        }
        return productionList;
    }

    public Map<String, Set<String>> getFollowFunction() {
        return followFunction;
    }

    public Map<String, Set<String>> getFirstFunction() {
        return firstFunction;
    }

    public String getFirstFunctionString() {
        StringBuilder stringBuilder = new StringBuilder("First Function: \n");
        for (Map.Entry<String, Set<String>> entry : firstFunction.entrySet()) {
            stringBuilder.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append('\n');
        }
        return stringBuilder.toString();
    }

    public String getFollowFunctionString() {
        StringBuilder stringBuilder = new StringBuilder("Follow Function: \n");
        for (Map.Entry<String, Set<String>> entry : followFunction.entrySet()) {
            stringBuilder.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append('\n');
        }
        return stringBuilder.toString();
    }
}
