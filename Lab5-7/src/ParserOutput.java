import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParserOutput {
    private Parser parser;
    private List<Integer> productionSequence;
    private String fileName;
    private boolean hasError;
    private List<String> sequenceOfDerivations;

    public ParserOutput(Parser parser, List<String> sequence, String fileName) {
        this.parser = parser;
        this.productionSequence = parser.parseSequence(sequence);
        this.fileName = fileName;
        this.hasError = productionSequence.contains(-1);
        this.sequenceOfDerivations = new ArrayList<>();
    }

    public void buildSequenceOfDerivations() {
        if (hasError)
            return;

        String currentSequence = parser.getGrammar().getStart();
        sequenceOfDerivations.add(currentSequence);
        var rhsOfProductions = parser.getRhsOfProductions();
        var lhsOfProductions = parser.getAssociatedNonTerminals();
        for (Integer productionNumber : productionSequence) {
            String nonTerminal = lhsOfProductions.get(productionNumber);
            List<String> rhs = rhsOfProductions.get(productionNumber - 1);

            int index = currentSequence.indexOf(nonTerminal);
            if (rhs.contains("epsilon")) {
                currentSequence = currentSequence.substring(0, index - 1) + currentSequence.substring(index + 1);
            }
            else {
                currentSequence = currentSequence.substring(0, index) + String.join(" ", rhs) + currentSequence.substring(index + 1);
            }
            sequenceOfDerivations.add(currentSequence);
        }
    }

    private String sequenceOfDerivationsToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sequenceOfDerivations.size() - 1; i++) {
            stringBuilder.append(sequenceOfDerivations.get(i)).append(" => (").append(productionSequence.get(i)).append(")\n");
        }
        stringBuilder.append(sequenceOfDerivations.get(sequenceOfDerivations.size() - 1));

        return stringBuilder.toString();
    }

    public void printSequenceOfDerivations() {
        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(sequenceOfDerivationsToString());
            bufferedWriter.close();
            System.out.println("Sequence of derivations: \n" + sequenceOfDerivationsToString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
