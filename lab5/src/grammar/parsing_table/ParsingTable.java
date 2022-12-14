package grammar.parsing_table;

import grammar.Production;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static grammar.Constants.EPSILON;

public class ParsingTable {
    private final Set<String> nonTerminals;
    private final Set<String> terminals;
    private final Set<String> symbols;
    private final Set<Production> productions;
    private Map<String, Set<String>> followFunction;
    private Map<String, Set<String>> firstFunction;
    private Map<String, Map<String, ParsingTableCell>> parsingTable;

    public ParsingTable(Set<String> nonTerminals, Set<String> terminals, Set<Production> productions, Map<String, Set<String>> followFunction, Map<String, Set<String>> firstFunction) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        this.followFunction = followFunction;
        this.firstFunction = firstFunction;
        parsingTable = new HashMap<>();
        symbols = new HashSet<>(nonTerminals);
        symbols.addAll(terminals);
        computeParsingTable();
    }

    private void computeParsingTable() {
        init();
        for(String nonTerminal : nonTerminals){
            Set<String> firstSet = firstFunction.get(nonTerminal);
            if(firstSet.contains(EPSILON));
        }
    }

    private void init(){
        for (String symbol : symbols) {
            for (String terminal : terminals) {
                put(symbol, terminal, ParsingTableCell.getErrorCell());
            }
        }
        for(String terminal : terminals){
            put(terminal,terminal, ParsingTableCell.getPopCell());
        }
        put("$", "$", ParsingTableCell.getAcceptCell());
    }

    public ParsingTableCell get(String firstSymbol, String secondSymbol) {
        return parsingTable.get(firstSymbol).get(secondSymbol);
    }

    private void put(String firstSymbol, String secondSymbol, ParsingTableCell parsingTableCell) {
        parsingTable.get(firstSymbol).put(secondSymbol, parsingTableCell);
    }
}
