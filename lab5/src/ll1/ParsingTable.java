package ll1;

import grammar.Grammar;
import grammar.Production;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static ll1.FirstAndFollowCalculator.concatenationOfLengthOne;
import static utils.Constants.EPSILON;


public class ParsingTable {
    private final Grammar grammar;
    private final Set<String> symbols;
    private final FirstAndFollowCalculator firstAndFollowCalculator;
    private final Map<String, Map<String, ParsingTableCell>> parsingTable;
    private final Map<Production, Integer> productionIndex;

    public ParsingTable(Grammar grammar, FirstAndFollowCalculator firstAndFollowCalculator) {
        this.grammar = grammar;
        this.firstAndFollowCalculator = firstAndFollowCalculator;
        parsingTable = new HashMap<>();
        productionIndex = initProductionIndex();

        symbols = new HashSet<>(grammar.getNonTerminals());
        symbols.addAll(grammar.getTerminals());
        symbols.add("$");
        computeParsingTable();
    }

    private Map<Production, Integer> initProductionIndex() {
        Map<Production, Integer> mapProductionToIndex = new HashMap<>();
        int index = 0;
        for (Production production : grammar.getProductions()) {
            mapProductionToIndex.put(production, index);
            index++;
        }
        return mapProductionToIndex;
    }

    private void computeParsingTable() {
        init();
        for (Production production : grammar.getProductions()) {
            Set<String> firstOfRHS = concatenationOfLengthOne(production.getRightSide(), firstAndFollowCalculator.getFirstFunction());
            if (!firstOfRHS.contains(EPSILON)) {
                for (String symbol : firstOfRHS) {
                    put(production.getLeftSide(), symbol,
                            new ParsingTableCell(production.getRightSide(), productionIndex.get(production)));
                }
            } else {
                Set<String> followOfLHS = firstAndFollowCalculator.getFollowFunction().get(production.getLeftSide());
                for (String symbol : followOfLHS) {
                    if (Objects.equals(symbol, EPSILON)) {
                        put(production.getLeftSide(), "$",
                                new ParsingTableCell(production.getRightSide(), productionIndex.get(production)));
                    } else {
                        put(production.getLeftSide(), symbol,
                                new ParsingTableCell(production.getRightSide(), productionIndex.get(production)));
                    }
                }
            }
        }

        for (String terminal : grammar.getTerminals()) {
            parsingTable.get(terminal).put(terminal, ParsingTableCell.getPopCell());
        }
        put("$", "$", ParsingTableCell.getAcceptCell());
    }

    private void init() {
        for (String symbol : symbols) {
            parsingTable.put(symbol, new HashMap<>());
        }
    }

    public ParsingTableCell get(String firstSymbol, String secondSymbol) {
        return parsingTable.get(firstSymbol).get(secondSymbol);
    }

    private void put(String firstSymbol, String secondSymbol, ParsingTableCell parsingTableCell) {
        ParsingTableCell existingCell = parsingTable.get(firstSymbol).put(secondSymbol, parsingTableCell);
        if (existingCell != null) {
            throw new RuntimeException("Conflict for M( " + firstSymbol + ", " + secondSymbol + ") ");
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Parsing table\n");
        grammar.getTerminals().add("$");
        for (String symbol : symbols) {
            for (String terminal : grammar.getTerminals()) {
                stringBuilder.append("M(")
                        .append(symbol)
                        .append(", ")
                        .append(terminal)
                        .append(")= ")
                        .append(get(symbol, terminal))
                        .append('\n');

            }
        }
        grammar.getTerminals().remove("$");
        return stringBuilder.toString();
    }
}
