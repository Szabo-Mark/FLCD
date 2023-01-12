package ll1;

import java.util.*;

public class Iteration {
    private final Map<String, Set<String>> followSets;

    public Iteration(Map<String, Set<String>> followSets) {
        this.followSets = followSets;
    }

    public Set<String> getFollowSet(String nonTerminal) {
        return followSets.get(nonTerminal);
    }

    public Map<String, Set<String>> getFollowSets() {
        return followSets;
    }

    public Iteration copy() {
        Map<String, Set<String>> copy = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : followSets.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return new Iteration(copy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Iteration iteration = (Iteration) o;
        return followSets.equals(iteration.followSets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followSets);
    }
}
