import java.util.Arrays;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar("src/Files/g1.txt");
        grammar.readGrammarFromFile();
        Parser parser1 = new Parser(grammar);

        System.out.println("Grammar = (\n" + grammar + ")\n");

        if (grammar.checkCFG()) {
            System.out.println("Grammar is CFG\n");
            parser1.computeFirst();
            parser1.computeFollow();
            System.out.println(parser1.firstToString());
            System.out.println(parser1.followToString());
        }
        else
            System.out.println("Grammar is not CFG\n");

        String nonTerminal = "A";
        if (grammar.getNonTerminals().contains(nonTerminal)) {
            System.out.print(grammar.printProductionsForNonTerminal(nonTerminal));
        }
        else {
            System.out.println("Non-terminal " + nonTerminal + " does not exist in the grammar");
        }
        System.out.println();

        Grammar grammar2 = new Grammar("src/Files/g2.txt");
        grammar2.readGrammarFromFile();
        Parser parser2 = new Parser(grammar2);

        System.out.println("Grammar = (\n" + grammar2 + ")\n");

        if (grammar2.checkCFG()) {
            System.out.println("Grammar2 is CFG\n");
            parser2.computeFirst();
            parser2.computeFollow();
            System.out.println(parser2.firstToString());
            System.out.println(parser2.followToString());
        }
        else
            System.out.println("Grammar2 is not CFG\n");
    }
}