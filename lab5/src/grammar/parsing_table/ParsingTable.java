package grammar.parsing_table;

import java.util.Map;

public class ParsingTable {
    Map<String, Map<String, ParsingTableCell>> parsingTable;

    public ParsingTableCell get(String firstSymbol, String secondSymbol){
        return parsingTable.get(firstSymbol).get(secondSymbol);
    }
}
