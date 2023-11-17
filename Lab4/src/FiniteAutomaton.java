import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FiniteAutomaton {
    private Set<String> states;
    private Set<String> alphabet;
    private Map<Pair<String, String>, Set<String>> transitions;
    private String initialState;
    private Set<String> finalStates;
    private String filename;

    public FiniteAutomaton(String filename) {
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
        this.transitions = new HashMap<>();
        this.finalStates = new HashSet<>();
        this.filename = filename;
    }

    public void readFAFromFile() {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            String readStates = scanner.nextLine();
            String[] split_states = readStates.split(" ");
            states.addAll(Arrays.asList(split_states));

            String readAlphabet = scanner.nextLine();
            String[] split_alphabet = readAlphabet.split(" ");
            alphabet.addAll(Arrays.asList(split_alphabet));

            initialState = scanner.nextLine();

            String readFinalStates = scanner.nextLine();
            String[] split_finalStates = readFinalStates.split(" ");
            finalStates.addAll(Arrays.asList(split_finalStates));

            while (scanner.hasNextLine()) {
                String readTransition = scanner.nextLine();
                String[] transitionElements = readTransition.split(" ");
                String state1 = transitionElements[0];
                String state2 = transitionElements[2];
                String symbol = transitionElements[1];

                if (states.contains(state1) && states.contains(state2) && alphabet.contains(symbol)) {
                    Pair<String, String> transition = new Pair<>(state1, symbol);
                    // If the key doesn't exist, create a new entry with a HashSet for the value
                    transitions.computeIfAbsent(transition, k -> new HashSet<>());
                    // Add the destination state to the set of the existing key
                    transitions.get(transition).add(state2);
                }
            }
        }
        catch (FileNotFoundException fe) {
            System.out.println("File not found");
        }
    }

    public String statesToString(Set<String> states, String typeOfStates) {
        StringBuilder statesString = new StringBuilder();
        statesString.append(typeOfStates).append(": ");
        for (String state : states) {
            statesString.append(state).append(" ");
        }
        return statesString.toString();
    }

    public String alphabetToString() {
        StringBuilder alphabetString = new StringBuilder();
        alphabetString.append("Alphabet: ");
        for (String symbol : alphabet) {
            alphabetString.append(symbol).append(" ");
        }
        return alphabetString.toString();
    }

    public String transitionsToString() {
        StringBuilder transitionsString = new StringBuilder();
        transitionsString.append("Transitions:\n");
        transitions.forEach((key, value) -> {
            transitionsString.append("(").append(key.getKey()).append(", ").append(key.getValue()).append(")").append(" -> ");
            for (String state : value) {
                transitionsString.append(state).append(" ");
            }
            transitionsString.append("\n");
        });

        return transitionsString.toString();
    }

    public boolean checkSequence(String sequence) {
        if (sequence.isEmpty()) {
            return finalStates.contains(initialState);
        }

        String state = initialState;
        for (int i = 0; i < sequence.length(); i++) {
            String symbol = String.valueOf(sequence.charAt(i));
            Pair<String, String> transition = new Pair<>(state, symbol);
            if (transitions.containsKey(transition)) {
                state = transitions.get(transition).iterator().next();
            }
            else {
                return false;
            }
        }

        return finalStates.contains(state);
    }

    public boolean isDFA() {
        return this.transitions.values().stream().noneMatch(set -> set.size() > 1);
    }

    public Set<String> getStates() {
        return this.states;
    }

    public Set<String> getFinalStates() {
        return this.finalStates;
    }

    public String getInitialState() {
        return this.initialState;
    }

    @Override
    public String toString() {
        return "FA = {\n" + statesToString(states, "States") + ",\n" + alphabetToString() + ",\n" + transitionsToString() + "Initial state: " + initialState + ",\n" + statesToString(finalStates, "Final states") + "\n}";
    }
}
