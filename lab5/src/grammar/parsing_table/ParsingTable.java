package grammar.parsing_table;

import grammar.Production;

import java.util.*;

public class ParsingTable {
    private final Set<String> nonTerminals;
    private final Set<String> terminals;
    private final Set<Production> productions;
    private Map<String, Map<String, ParsingTableCell>> parsingTable;

    public ParsingTable(Set<String> nonTerminals, Set<String> terminals, Set<Production> productions) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        parsingTable = new HashMap<>();
        Set<String> symbols = new HashSet<>(nonTerminals);
        symbols.addAll(terminals);
        for(String symbol : symbols){
            for(String terminal : terminals){
            }
        }
        put("$", "$", new ParsingTableCell(List.of("ACC"), 0));
    }


    public ParsingTableCell get(String firstSymbol, String secondSymbol) {
        return parsingTable.get(firstSymbol).get(secondSymbol);
    }

    public void put(String firstSymbol, String secondSymbol, ParsingTableCell parsingTableCell) {
        parsingTable.get(firstSymbol).put(secondSymbol, parsingTableCell);
    }
}
