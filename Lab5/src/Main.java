public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar("src/Files/g1.txt");
        grammar.readGrammarFromFile();

        System.out.println("Grammar = (\n" + grammar + ")\n");

        if (grammar.checkCFG())
            System.out.println("Grammar is CFG\n");
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

        System.out.println("Grammar = (\n" + grammar2 + ")\n");

        if (grammar2.checkCFG())
            System.out.println("Grammar2 is CFG\n");
        else
            System.out.println("Grammar2 is not CFG\n");
    }
}