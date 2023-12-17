import java.util.*;

public class Parser {

    private Grammar grammar;
    private HashMap<String, Set<String>> first;
    private HashMap<String, Set<String>> follow;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        this.first = new HashMap<>();
        this.follow = new HashMap<>();
    }

    public Set<String> concatOfLengthOne(Set<String> set1, Set<String> set2) {
        Set<String> resultSet = new HashSet<>();
        for (String s1 : set1) {
            for (String s2 : set2) {
                if (Objects.equals(s1, "epsilon") && !Objects.equals(s2, "epsilon")) {
                    resultSet.add(s2.charAt(0) + "");
                } else if (Objects.equals(s2, "epsilon") && !Objects.equals(s1, "epsilon")) {
                    resultSet.add(s1.charAt(0) + "");
                } else if (Objects.equals(s1, "epsilon") && Objects.equals(s2, "epsilon")) {
                   resultSet.add("epsilon");
                }
                else
                    resultSet.add((s1 + s2).charAt(0) + "");
            }
        }
        return resultSet;
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
}
