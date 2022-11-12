package scanner.FiniteAutomata;

import scanner.FiniteAutomata.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class TransitionFunction {
    private final Map<String, Set<Pair<String, String>>> transitionFunction;

    public TransitionFunction() {
        transitionFunction = new HashMap<>();
    }

    public void add(String source, String inputSymbol, String destination) {
        if (transitionFunction.containsKey(source)) {
            transitionFunction.get(source).add(new Pair<>(inputSymbol, destination));
        } else {
            Set<Pair<String, String>> set = new HashSet<>();
            set.add(new Pair<>(inputSymbol, destination));
            transitionFunction.put(source, set);
        }
    }

    public Optional<String> getResult(String state, String symbol) {
        Set<Pair<String, String>> set = transitionFunction.get(state);
        if (set == null) {
            return Optional.empty();
        } else {
            return set.stream()
                    .filter(pair -> Objects.equals(pair.getFirstElem(), symbol))
                    .map(Pair::getSecondElem)
                    .findFirst();
        }
    }

    public boolean isDFA() {
        for (String key : transitionFunction.keySet()) {
            List<String> symbols = transitionFunction.get(key).stream()
                    .map(Pair::getFirstElem)
                    .collect(Collectors.toList());
            Set<String> symbolsSet = new HashSet<>(symbols);
            if (symbols.size() != symbolsSet.size()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("TRANSITION_FUNCTION: \n");
        for (String key : transitionFunction.keySet()) {
            for (Pair<String, String> pair : transitionFunction.get(key)) {
                stringBuilder.append(key)
                        .append(',')
                        .append(pair.getFirstElem())
                        .append('=')
                        .append(pair.getSecondElem())
                        .append('\n');
            }
        }
        return stringBuilder.toString();
    }
}
