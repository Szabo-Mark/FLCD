import grammar.Grammar;

public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar("resources/grammar.txt");
//        grammar.showMenu();
        grammar.first();
    }
}
