import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FiniteAutomata {
    private final Set<String> states;
    private final Set<String> symbols;
    private String initialState;
    private final Set<String> finalStates;
    private final TransitionFunction transitionFunction;

    public FiniteAutomata(Set<String> states, Set<String> symbols, String initialState, Set<String> finalStates, TransitionFunction transitionFunction) {
        this.states = states;
        this.symbols = symbols;
        this.initialState = initialState;
        this.finalStates = finalStates;
        this.transitionFunction = transitionFunction;
    }

    public FiniteAutomata() {
        states = new HashSet<>();
        symbols = new HashSet<>();
        initialState = null;
        finalStates = new HashSet<>();
        transitionFunction = new TransitionFunction();
    }

    public void readFromFile(String pathToFile) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToFile))) {
            StringBuilder errorStringBuilder = new StringBuilder("Errors\n");
            FAFileDelimiter lastFileDelimiter = null;
            FAFileDelimiter faFileDelimiter = null;
            String line = bufferedReader.readLine();
            int lineCount = 1;
            while (line != null) {
                faFileDelimiter = checkIfDelimiter(line);
                if (faFileDelimiter != null) {
                    lastFileDelimiter = faFileDelimiter;
                } else {
                    Optional<String> error = handleLine(lastFileDelimiter, line);
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
            System.out.println(errorStringBuilder);
        } catch (IOException exception) {
            System.out.println("Token file could not be parsed!");
        }
    }

    public boolean isAccepted(List<String> sequence) {
        String currentState = initialState;
        for (String symbol : sequence) {
            Optional<String> optional = transitionFunction.getResult(currentState, symbol);
            if (optional.isEmpty()) {
                return false;
            }
            currentState = optional.get();
        }
        return finalStates.contains(currentState);
    }

    public boolean isDFA() {
        return transitionFunction.isDFA();
    }

    private Optional<String> handleLine(FAFileDelimiter faFileDelimiter, String line) {
        return switch (faFileDelimiter) {
            case SYMBOLS -> handleSymbols(line);
            case INITIAL_STATE -> handleInitialState(line);
            case FINAL_STATES -> handleFinalStates(line);
            case STATES -> handleStates(line);
            case TRANSITION_FUNCTION -> handleTransitionFunction(line);
        };
    }

    private Optional<String> handleSymbols(String line) {
        symbols.add(line);
        return Optional.empty();
    }

    private Optional<String> handleStates(String line) {
        states.add(line);
        return Optional.empty();
    }

    private Optional<String> handleInitialState(String line) {
        if (initialState != null) {
            return Optional.of("Cannot have more than one initial state");
        }
        initialState = line;
        return Optional.empty();
    }

    private Optional<String> handleFinalStates(String line) {
        finalStates.add(line);
        return Optional.empty();
    }

    private Optional<String> handleTransitionFunction(String line) {
        String[] tokens = line.split("[,=]");
        if (tokens.length != 3) {
            return Optional.of("Wrong format for transition function!");
        }
        transitionFunction.add(tokens[0], tokens[1], tokens[2]);
        return Optional.empty();
    }

    private FAFileDelimiter checkIfDelimiter(String line) {
        if (Objects.equals(line, FAFileDelimiter.STATES.toString())) {
            return FAFileDelimiter.STATES;
        }
        if (Objects.equals(line, FAFileDelimiter.SYMBOLS.toString())) {
            return FAFileDelimiter.SYMBOLS;
        }
        if (Objects.equals(line, FAFileDelimiter.INITIAL_STATE.toString())) {
            return FAFileDelimiter.INITIAL_STATE;
        }
        if (Objects.equals(line, FAFileDelimiter.FINAL_STATES.toString())) {
            return FAFileDelimiter.FINAL_STATES;
        }
        if (Objects.equals(line, FAFileDelimiter.TRANSITION_FUNCTION.toString())) {
            return FAFileDelimiter.TRANSITION_FUNCTION;
        }
        return null;
    }

    public Set<String> getStates() {
        return states;
    }

    public Set<String> getSymbols() {
        return symbols;
    }

    public String getInitialState() {
        return initialState;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    public TransitionFunction getTransitionFunction() {
        return transitionFunction;
    }
}
