import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Language {
    private List<String> reservedWords = Arrays.asList("def", "int", "string", "whether", "otherwise", "Array", "whileLoop", "forLoop", "const", "input", "out", "len");
    private List<String> separators = Arrays.asList("\\", "/", "~", ";", ":", " ", "(", ")", "{", "}", "[", "]");
    private List<String> operators = Arrays.asList("+", "-", "*", "/", "<-", "->", ">>", "<<", "==", "!!", ">>=", "<<=", "=+", "=-", "=*", "=/", ".", "and", "or");

    private FiniteAutomaton identifierFA;
    private FiniteAutomaton integerFA;

    public Language() {
        identifierFA = new FiniteAutomaton("src/Files/identifierFA.in");
        integerFA = new FiniteAutomaton("src/Files/integerFA.in");
        identifierFA.readFAFromFile();
        integerFA.readFAFromFile();
    }

    public boolean isReservedWord(String word) {
        return reservedWords.contains(word);
    }

    public boolean isSeparator(String word) {
        return separators.contains(word);
    }

    public boolean isOperator(String word) {
        return operators.contains(word);
    }

    public boolean isIdentifier(String token) {
        return identifierFA.isSequenceAccepted(token);
    }

    public boolean isConstant(String token) {
        // String numericPattern = "^0|[-][1-9]([0-9])*|[1-9]([0-9])*$";
        String stringPattern = "^`[a-zA-Z0-9_*./%+=;?!#()]+`$";

        return integerFA.isSequenceAccepted(token) || token.matches(stringPattern);
    }
}
