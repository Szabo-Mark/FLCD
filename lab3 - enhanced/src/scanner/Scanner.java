package scanner;

import scanner.FiniteAutomata.FiniteAutomata;
import scanner.programInternalFormTable.ProgramInternalFormTable;
import scanner.symbolTable.Position;
import scanner.symbolTable.SymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Scanner {
    private final static String SEPARATORS = " |;|\\{|}|\\[|]|!|<|>|=|-|\\+|\\*|/|%|:|\t";
    private final static String SEPARATORS_REGEX = "((?=" + SEPARATORS + ")|(?<=" + SEPARATORS + "))";
    private final static String INTEGER_REGEX = "^0|[+|-]?[1-9][0-9]*$";
    private final static String CHARACTER_REGEX = "^'[a-zA-Z0-9]'$";
    private final static String IDENTIFIER_REGEX = "^([a-zA-Z][a-zA-Z\\d]*)$";
    private final static Map<String, Set<String>> COMPOUND_SEPARATORS = new HashMap<>(Map.of(
            "!", Set.of("="),
            "<", Set.of("=", "<"),
            ">", Set.of("=", ">")
    ));
    private final static String CONSTANT = "const";
    private final static String IDENTIFIER = "id";
    private final static String PATH_TO_CONSTANT_CHARACTER_FA = "resources/fa/constantFA_Character.in";
    private final static String PATH_TO_CONSTANT_INTEGER_FA = "resources/fa/constantFA_Integer.in";
    private final static String PATH_TO_IDENTIFIER_FA = "resources/fa/identifierFA.in";

    private final SymbolTable identifiers;
    private final SymbolTable constants;
    private final ProgramInternalFormTable pif;
    private final FiniteAutomata constantCharacterFA;
    private final FiniteAutomata constantIntegerFA;
    private final FiniteAutomata identifierFA;
    private Set<String> TOKENS;

    public Scanner() {
        this.identifiers = new SymbolTable();
        this.constants = new SymbolTable();
        this.pif = new ProgramInternalFormTable();
        constantCharacterFA = new FiniteAutomata(PATH_TO_CONSTANT_CHARACTER_FA);
        constantIntegerFA = new FiniteAutomata(PATH_TO_CONSTANT_INTEGER_FA);
        identifierFA = new FiniteAutomata(PATH_TO_IDENTIFIER_FA);
    }

    public void run(String pathToSourceFile, String pathToTokenFile) {
        TOKENS = parseTokenFile(pathToTokenFile);
        String lexicalErrors = parseSourceFile(pathToSourceFile);
        if (lexicalErrors.isEmpty()) {
            System.out.println("Lexically correct!");
            writePIF("resources/out/PIF.out", pif);
            writeSymbolTable("resources/out/ST_constants.out", constants);
            writeSymbolTable("resources/out/ST_identifiers.out", identifiers);
        } else {
            System.out.println(lexicalErrors);
        }
    }

    private Set<String> parseTokenFile(String pathToTokenFile) {
        Set<String> tokenSet = new HashSet<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToTokenFile))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                tokenSet.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException exception) {
            System.out.println("Token file could not be parsed!");
        }
        return tokenSet;
    }

    private String parseSourceFile(String pathToSourceFile) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToSourceFile))) {
            int i = 1;
            String line = bufferedReader.readLine();
            while (line != null) {
                List<String> tokens = parseLine(line);
                stringBuilder.append(addTokensToTables(tokens, i));
                line = bufferedReader.readLine();
                i++;
            }
            return stringBuilder.toString();
        } catch (IOException exception) {
            System.out.println("Source file could not be parsed!");
        }
        return stringBuilder.toString();
    }

    private List<String> parseLine(String line) {
        String[] tokens = line.split(SEPARATORS_REGEX);
        List<String> tokenList = new ArrayList<>(List.of(tokens));
        int i = 0;
        while (i < tokenList.size() - 1) {
            String token1 = tokenList.get(i);
            if (Objects.equals(token1, " ") || Objects.equals(token1, "\t")) {
                tokenList.remove(token1);
            } else {
                String token2 = tokenList.get(i + 1);
                if (isCompoundSeparator(token1, token2)) {
                    String compoundSeparator = token1 + token2;
                    tokenList.remove(i);
                    tokenList.remove(i);
                    tokenList.add(i, compoundSeparator);
                }
                i++;
            }
        }
        return tokenList;
    }

    private boolean isCompoundSeparator(String token1, String token2) {
        if (COMPOUND_SEPARATORS.containsKey(token1)) {
            Set<String> setOfPossibleCompoundSeparators = COMPOUND_SEPARATORS.get(token1);
            return setOfPossibleCompoundSeparators.contains(token2);
        }
        return false;
    }

    private String addTokensToTables(List<String> tokens, int lineNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        tokens.forEach((token) -> {
            if (TOKENS.contains(token)) {
                pif.addToList(token, new Position(-1, -1));
            } else {
                if (constantIntegerFA.accepts(token) || constantCharacterFA.accepts(token)) {
                    Position position = constants.add(token);
                    pif.addToList(CONSTANT, position);
                } else {
                    if (identifierFA.accepts(token)) {
                        Position position = identifiers.add(token);
                        pif.addToList(IDENTIFIER, position);
                    } else {
                        stringBuilder.append("Token '")
                                .append(token)
                                .append("' on line ")
                                .append(lineNumber)
                                .append(" is not lexically valid!")
                                .append("\n");
                    }
                }
            }
        });
        return stringBuilder.toString();
    }

    private void writePIF(String pathToFile, ProgramInternalFormTable pif) {
        try (FileWriter fileWriter = new FileWriter(pathToFile)) {
            fileWriter.write(pif.toString());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void writeSymbolTable(String pathToFile, SymbolTable symbolTable) {
        try (FileWriter fileWriter = new FileWriter(pathToFile)) {
            fileWriter.write(symbolTable.toString());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
