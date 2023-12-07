import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grammar {
    private Set<String> nonTerminals;
    private Set<String> terminals;
    private HashMap<String, Set<List<String>>> productions;
    private String startSymbol;

    private String filename;

    public Grammar(String filename) {
        this.filename = filename;
        this.nonTerminals = new HashSet<>();
        this.terminals = new HashSet<>();
        this.productions = new HashMap<>();
    }

    private Set<String> parseLine(String line) {
        Set<String> resultSet = new HashSet<>();

        int startIndex = line.indexOf('{');
        int endIndex = line.lastIndexOf('}');

        // Check if both curly braces are found
        if (startIndex != -1 && endIndex != -1) {
            String elements = line.substring(startIndex + 1, endIndex);

            // Split the elements based on commas and optional spaces
            String[] elementArray = elements.split("\\s*,\\s*");

            // Add elements to the set
            for (String element : elementArray) {
                resultSet.add(element.trim());
            }
        }

        return resultSet;
    }

    public void readGrammarFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            nonTerminals = parseLine(line);

            line = reader.readLine();
            terminals = parseLine(line);

            startSymbol = reader.readLine().split("= ")[1].strip();

            reader.readLine(); // Skip the line with the P = { header
            line = reader.readLine();
            while (line != null) {
                if (!line.equals("}")) {
                    String[] splitLine = line.split("->", 2);
                    String leftHandSide = splitLine[0].strip();
                    String[] rightHandSide = splitLine[1].split("\\|");

                    // Add the production to the set of productions for the left side
                    productions.computeIfAbsent(leftHandSide, k -> new HashSet<>());

                    // Add the right side to the set of productions for the left side
                    for (String prod : rightHandSide) {
                        String[] prodElements = prod.strip().split("\\s+");
                        productions.get(leftHandSide).add(List.of(prodElements));
                    }
                }

                line =  reader.readLine();
            }

        }
        catch (FileNotFoundException fe) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    public String printNonTerminals() {
        StringBuilder builder = new StringBuilder();
        builder.append("Non-terminals: { ");
        for (String nonTerminal : nonTerminals) {
            builder.append(nonTerminal).append(" ");
        }
        builder.append("}");

        return builder.toString();
    }

    public String printTerminals() {
        StringBuilder builder = new StringBuilder();
        builder.append("Terminals: { ");
        for (String terminal : terminals) {
            builder.append(terminal).append(" ");
        }
        builder.append("}");

        return builder.toString();
    }

    public String printProductions() {
        StringBuilder builder = new StringBuilder();
        builder.append("Productions = { \n");
        productions.forEach((left, right) -> {
            builder.append("\t").append(left).append(" -> ");
            int count = 0;
            for(List<String> prodList : right){
                for(String r : prodList) {
                    builder.append(r).append(" ");
                }
                count++;
                if (count < right.size())
                    builder.append("| ");

            }
            builder.append("\n");
        });
        builder.append("}");
        return builder.toString();
    }

    public String printProductionsForNonTerminal(String nonTerminal) {
        StringBuilder builder = new StringBuilder();
        builder.append("Productions for ").append(nonTerminal).append(":\n");

        for(List<String> prodList : productions.get(nonTerminal)){
            builder.append(nonTerminal).append(" -> ");
            for(String r : prodList) {
                builder.append(r).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public String getStart() {
        return startSymbol;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public boolean checkCFG() {
        boolean startingSymbolInProductions = false;
        for (String entry : productions.keySet()) {
            if (entry.equals(startSymbol)) {
                startingSymbolInProductions = true;
                break;
            }
        }
        if (!startingSymbolInProductions) {
            return false;
        }

        for (String lhs: productions.keySet()) {
            if (!nonTerminals.contains(lhs)) {
                return false;
            }

            Set<List<String>> rhs = productions.get(lhs);

            for(List<String> prodList : rhs) {
                for (String str : prodList) {
                    if(!nonTerminals.contains(str) && !terminals.contains(str) && !str.equals("epsilon"))
                        return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return printNonTerminals() + "\n" +
                printTerminals() + "\n" +
                printProductions() + "\n" +
                "Start symbol: " + getStart() + "\n";
    }

}
