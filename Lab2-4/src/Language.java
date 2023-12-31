import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Language {
    private List<String> reservedWords = Arrays.asList("def", "int", "string", "whether", "otherwise", "Array", "whileLoop", "forLoop", "const", "input", "out", "len");
    private List<String> separators = Arrays.asList("\\", "/", "~", ";", ":", " ", "(", ")", "{", "}", "[", "]");
    private List<String> operators = Arrays.asList("+", "-", "*", "/", "<-", "->", ">>", "<<", "==", "!!", ">>=", "<<=", "=+", "=-", "=*", "=/", ".", "and", "or");


    public Language() {}

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
        return token.matches("^[a-zA-Z]([a-zA-Z0-9]*)$");
    }

    public boolean isConstant(String token) {
        String numericPattern = "^0|[-][1-9]([0-9])*|[1-9]([0-9])*$";
        String stringPattern = "^`[a-zA-Z0-9_*./%+=;?!#()]+`$";

        return token.matches(numericPattern) || token.matches(stringPattern);
    }
}
