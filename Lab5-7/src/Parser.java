import java.util.*;

public class Parser {

    private Grammar grammar;
    private HashMap<String, Set<String>> first;
    private HashMap<String, Set<String>> follow;
    private List<List<String>> rhsOfProductions;
    private HashMap<Pair<String, String>, Pair<String, Integer>> parseTable;
    private HashMap<Integer, String> associatedNonTerminals;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        this.first = new HashMap<>();
        this.follow = new HashMap<>();
        this.parseTable = new HashMap<>();
    }

    public void computeFirst() {
        // Initialize first sets -> F0(A)
        for (String nonTerminal : grammar.getNonTerminals()) {
            first.put(nonTerminal, new HashSet<>());
            Set<List<String>> productionForNonTerminal = grammar.getProductionsForNonTerminal(nonTerminal);
            for (List<String> production : productionForNonTerminal) {
                if (grammar.getTerminals().contains(production.get(0)) || production.get(0).equals("epsilon"))
                    first.get(nonTerminal).add(production.get(0));
            }
        }

        boolean isChanged = true;

        while (isChanged) {
            isChanged = false;

            for (String nonTerminal : grammar.getNonTerminals()) {
                Set<String> previousFirstSet = new HashSet<>(first.get(nonTerminal));

                Set<List<String>> productionsForNonTerminal = grammar.getProductionsForNonTerminal(nonTerminal);

                for (List<String> production : productionsForNonTerminal) {
                    Set<String> symbolsToAdd = new HashSet<>();

                    for (String symbol : production) {
                        if (this.grammar.getTerminals().contains(symbol)) {
                            symbolsToAdd.add(symbol);
                            break;
                        }
                        else {
                            Set<String> firstSetOfSymbol = new HashSet<>(first.getOrDefault(symbol, Collections.emptySet()));
                            symbolsToAdd.addAll(firstSetOfSymbol);

                            if (!firstSetOfSymbol.contains("epsilon")) {
                                break;
                            }
                        }
                    }

                    first.get(nonTerminal).addAll(symbolsToAdd);
                }

                if (!previousFirstSet.equals(first.get(nonTerminal))) {
                    isChanged = true;
                }
            }
        }
    }

    public String firstToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FIRST SETS:\n");
        for (String nonTerminal: first.keySet()) {
            builder.append("FIRST(").append(nonTerminal).append(") = { ");
            int count = 0;
            for (String terminal: first.get(nonTerminal)) {
                builder.append(terminal);
                count++;
                if (count < first.get(nonTerminal).size())
                    builder.append(", ");
            }
            builder.append(" }\n");
        }

        return builder.toString();
    }

    public void computeFollow() {
        for (String nonTerminal : grammar.getNonTerminals()) {
            follow.put(nonTerminal, new HashSet<>());
        }
        follow.get(grammar.getStart()).add("epsilon");

        boolean isChanged = true;
        while (isChanged) {
            isChanged = false;
            HashMap<String, Set<String>> currentFollow = new HashMap<>();

            for (String nonTerminal : grammar.getNonTerminals()) {
                currentFollow.put(nonTerminal, new HashSet<>());
                HashMap<String, Set<List<String>>> productionsWithNonTerminalInRhs = new HashMap<>();
                HashMap<String, Set<List<String>>> productions = grammar.getProductions();

                productions.forEach((lhs, rhs) -> {
                    for (var prod: rhs) {
                        if (prod.contains(nonTerminal)) {
                            if (!productionsWithNonTerminalInRhs.containsKey(lhs)) {
                                productionsWithNonTerminalInRhs.put(lhs, new HashSet<>());
                            }
                            productionsWithNonTerminalInRhs.get(lhs).add(prod);
                        }
                    }
                });

                HashSet<String> toAdd = new HashSet<>(follow.get(nonTerminal));
                productionsWithNonTerminalInRhs.forEach((lhs, rhs) -> {
                    for (List<String> prod: rhs) {
                        var prodList = new ArrayList<>(prod);
                        for (int index = 0; index < prodList.size(); index++) {
                            if (prodList.get(index).equals(nonTerminal)) {
                                if (index == prodList.size() - 1) {
                                    toAdd.addAll(follow.get(lhs));
                                }
                                else {
                                    String followingSymbol = prodList.get(index + 1);
                                    if (grammar.getTerminals().contains(followingSymbol)) {
                                        toAdd.add(followingSymbol);
                                    }
                                    else {
                                        for (String symbol: first.get(followingSymbol)) {
                                            if (!symbol.equals("epsilon")) {
                                                toAdd.addAll(first.get(followingSymbol));
                                            }
                                            else {
                                                toAdd.addAll(follow.get(lhs));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });

                if (!toAdd.equals(follow.get(nonTerminal))) {
                    isChanged = true;
                }
                currentFollow.put(nonTerminal, toAdd);
            }

            follow = currentFollow;
        }
    }

    public String followToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FOLLOW SETS:\n");
        for (String nonTerminal: follow.keySet()) {
            builder.append("FOLLOW(").append(nonTerminal).append(") = { ");
            int count = 0;
            for (String terminal: follow.get(nonTerminal)) {
                builder.append(terminal);
                count++;
                if (count < follow.get(nonTerminal).size())
                    builder.append(", ");
            }
            builder.append(" }\n");
        }

        return builder.toString();
    }

    public void computeRhsOfProductions() {
        rhsOfProductions = new ArrayList<>();
        associatedNonTerminals = new HashMap<>();
        final int[] count = {0};
        grammar.getProductions().forEach((nonTerminal, rhs) -> {
            for (var prod: rhs) {
                if (!prod.get(0).equals("epsilon")) {
                    rhsOfProductions.add(prod);
                }
                else
                    rhsOfProductions.add(new ArrayList<>(List.of("epsilon", nonTerminal)));
                count[0]++;
                associatedNonTerminals.put(count[0], nonTerminal);
            }
        });
    }

    public List<List<String>> getRhsOfProductions() {
        return this.rhsOfProductions;
    }

    public HashMap<Integer, String> getAssociatedNonTerminals() {
        return this.associatedNonTerminals;
    }

    public void printRhsOfProductions() {
        System.out.println("Right hand side of productions:");
        for (int i = 0; i < rhsOfProductions.size(); i++) {
            if (rhsOfProductions.get(i).contains("epsilon"))
                System.out.println("epsilon, " + (i+1));
            else
                System.out.println(String.join(" ", rhsOfProductions.get(i)) + ", " + (i+1));
        }
        System.out.println();
    }

    public void createParseTable() {
        List<String> rows = new ArrayList<>();
        rows.addAll(grammar.getNonTerminals());
        rows.addAll(grammar.getTerminals());
        rows.add("$");
        List<String> columns = new ArrayList<>(grammar.getTerminals());
        columns.add("$");

        for (String row: rows)
            for (String col: columns)
                if (row.equals(col))
                    parseTable.put(new Pair<>(row, col), new Pair<>("pop", -1));
                else
                    parseTable.put(new Pair<>(row, col), new Pair<>("err", -1));
        parseTable.put(new Pair<>("$", "$"), new Pair<>("acc", -1));

        var productions = grammar.getProductions();
        computeRhsOfProductions();
        printRhsOfProductions();

        productions.forEach((nonTerminal, rhs) -> {
            for (var production : rhs) {
                String firstSymbol = production.get(0);
                if (grammar.getTerminals().contains(firstSymbol))
                    if (parseTable.get(new Pair<>(nonTerminal, firstSymbol)).getFirst().equals("err"))
                        parseTable.put(new Pair<>(nonTerminal, firstSymbol), new Pair<>(String.join(" ", production), rhsOfProductions.indexOf(production) + 1));
                    else {
                        handleConflict(nonTerminal, firstSymbol);
                    }
                else if (grammar.getNonTerminals().contains(firstSymbol)) {
                    if (production.size() == 1)
                        for (String symbol : first.get(firstSymbol))
                            if (parseTable.get(new Pair<>(nonTerminal, symbol)).getFirst().equals("err"))
                                parseTable.put(new Pair<>(nonTerminal, symbol), new Pair<>(String.join(" ", production),rhsOfProductions.indexOf(production) + 1));
                            else {
                                handleConflict(nonTerminal, symbol);
                            }
                    else {
                        int i = 1;
                        String nextSymbol = production.get(1);
                        var firstSetForProduction = first.get(firstSymbol);

                        while (i < production.size() && grammar.getNonTerminals().contains(nextSymbol)) {
                            var firstForNext = first.get(nextSymbol);
                            if (firstSetForProduction.contains("epsilon")) {
                                firstSetForProduction.remove("epsilon");
                                firstSetForProduction.addAll(firstForNext);
                            }
                            i++;
                            if (i < production.size())
                                nextSymbol = production.get(i);
                        }

                        for (var symbol : firstSetForProduction) {
                            if (symbol.equals("epsilon"))
                                symbol = "$";
                            if (parseTable.get(new Pair<>(nonTerminal, symbol)).getFirst().equals("err"))
                                parseTable.put(new Pair<>(nonTerminal, symbol), new Pair<>(String.join(" ", production), rhsOfProductions.indexOf(production) + 1));
                            else {
                                handleConflict(nonTerminal, symbol);
                            }
                        }
                    }
                }
                else {
                    Set<String> followSet = follow.get(nonTerminal);
                    for (var symbol : followSet) {
                        if (symbol.equals("epsilon")) {
                            Pair<String, String> epsilonPosition = new Pair<>(nonTerminal, "$");
                            if (parseTable.get(epsilonPosition).getFirst().equals("err")) {
                                List<String> prod = new ArrayList<>(List.of("epsilon", nonTerminal));
                                parseTable.put(epsilonPosition, new Pair<>("epsilon", rhsOfProductions.indexOf(prod) + 1));
                            } else {
                                handleConflict(nonTerminal, "epsilon");
                            }
                        } else {
                            Pair<String, String> position = new Pair<>(nonTerminal, symbol);
                            if (parseTable.get(position).getFirst().equals("err")) {
                                List<String> prod = new ArrayList<>(List.of("epsilon", nonTerminal));
                                parseTable.put(position, new Pair<>("epsilon", rhsOfProductions.indexOf(prod) + 1));
                            } else {
                                handleConflict(nonTerminal, symbol);
                            }
                        }
                    }
                }
            }
        });
    }

    private void handleConflict(String nonTerminal, String symbol) {
        try {
            throw new Exception("CONFLICT in the cell: " + nonTerminal + ", " + symbol);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseTableToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PARSE TABLE:\n");
        List<String> rows = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        rows.addAll(grammar.getNonTerminals());
        rows.addAll(grammar.getTerminals());
        rows.add("$");
        columns.addAll(grammar.getTerminals());
        columns.add("$");

        builder.append(String.format("%-10s", ""));
        for (String column : columns) {
            builder.append(String.format("%-15s", "   " + column));
        }
        builder.append("\n");

        // Table content
        for (String row : rows) {
            builder.append(String.format("%-10s", row));
            for (String column : columns) {
                Pair position = new Pair(row, column);
                Pair value = parseTable.get(position);
                if (value.getFirst() == "err")
                    builder.append(String.format("%-15s", ""));
                else if (value.getFirst() == "pop")
                    builder.append(String.format("%-15s", "pop"));
                else if (value.getFirst() == "acc")
                    builder.append(String.format("%-15s", "acc"));
                else
                    builder.append(String.format("%-15s", value));
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public List<Integer> parseSequence(String sequence) {
        Stack<String> inputStack = new Stack<>();
        Stack<String> workingStack = new Stack<>();
        List<Integer> output = new ArrayList<>();

        // initialize input stack
        inputStack.push("$");
        String[] sequenceSymbols = sequence.split(" ");
        for (int i = sequenceSymbols.length - 1; i >= 0; i--) {
            inputStack.push(sequenceSymbols[i]);
        }

        // initialize working stack
        workingStack.push("$");
        workingStack.push(grammar.getStart());

        boolean go = true;
        String result = "";

        while (go) {
            String inputTop = inputStack.peek();
            String workingTop = workingStack.peek();
            Pair<String, String> positionInParseTable = new Pair<>(workingTop, inputTop);
            Pair<String, Integer> valueInParseTable = parseTable.get(positionInParseTable);

            if (valueInParseTable.getFirst().equals("acc")) {
                go = false;
                result = "acc";
            }
            else if (valueInParseTable.getFirst().equals("err")) {
                go = false;
                result = "err";
            }
            else {
                if (valueInParseTable.getFirst().equals("pop")) {
                    inputStack.pop();
                    workingStack.pop();
                }
                else {
                    workingStack.pop();
                    if(!valueInParseTable.getFirst().equals("epsilon")) {
                        String[] symbols = valueInParseTable.getFirst().split(" ");
                        for (int i = symbols.length - 1; i >= 0; i--)
                            workingStack.push(symbols[i]);
                    }
                    output.add(valueInParseTable.getSecond());
                }
            }
        }

        if (result.equals("acc")) {
            System.out.println("Sequence accepted.");
            return output;
        }
        // if result == "err"
        System.out.println("Sequence not accepted. Syntax error at: " + workingStack.peek() + ", " + inputStack.peek());
        return new ArrayList<>(List.of(-1));
    }

    public Grammar getGrammar() {
        return grammar;
    }
}
