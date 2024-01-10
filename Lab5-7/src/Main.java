import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class Main {
    public static List<String> readSequence(String filename) {
        List<String> sequence = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line = reader.readLine();
            while (line != null) {
                var symbols = List.of(line.split(" "));
                sequence.addAll(symbols);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return sequence;
    }

    public static List<String> readPIF(String filename) {
        try {
            List<String> tokens = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null){
                List<String> tokenAndPosition = Arrays.asList(line.split(" "));
                if (!tokenAndPosition.get(3).equals("-1")) {
                    if(tokenAndPosition.get(0).equals("const"))
                        tokens.add("constant");
                    else
                        tokens.add("identifier");
                }
                else
                    tokens.add(tokenAndPosition.get(0).strip());
                line = reader.readLine();
            }
            reader.close();
            return tokens;
        }
        catch (Exception e){
            return new ArrayList<>();
        }
    }

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
            try {
                parser1.createParseTable();
                System.out.println(parser1.parseTableToString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            ParserOutput parserOutput1 = new ParserOutput(parser1, readSequence("src/Files/seq.txt"), "src/Files/out1.txt");
            parserOutput1.buildSequenceOfDerivations();
            parserOutput1.printSequenceOfDerivations();
        }
        else
            System.out.println("Grammar is not CFG\n");

        System.out.println("--------------------------------------------------\n");

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
            try {
                parser2.createParseTable();
                System.out.println(parser2.parseTableToString());
                ParserOutput parserOutput2 = new ParserOutput(parser2, readPIF("src/Files/PIF.out"), "src/Files/out2.txt");
                parserOutput2.buildSequenceOfDerivations();
                parserOutput2.printSequenceOfDerivations();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else
            System.out.println("Grammar2 is not CFG\n");
    }
}