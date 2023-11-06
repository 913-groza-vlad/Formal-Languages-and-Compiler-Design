import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyScanner {
    private Language lang;
    private SymbolTable identifiersSymbolTable;
    private SymbolTable constantsSymbolTable;
    private PIF pif;

    private String program;
    private String PIFFile;
    private String symbolTableFile;

    public MyScanner(String program, String PIFFile, String symbolTableFile) {
        this.program = program;
        this.PIFFile = PIFFile;
        this.symbolTableFile = symbolTableFile;
        this.lang = new Language();
        this.identifiersSymbolTable = new SymbolTable(15);
        this.constantsSymbolTable = new SymbolTable(15);
        this.pif = new PIF();
    }

    public void scan() {
        List<Pair<String, Integer>> tokenPairs = new ArrayList<>();
        try {
            File file = new File(program);
            Scanner scanner = new Scanner(file);

            int lineNumber = 1;

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if (!line.startsWith("//")) {
                    List<String> tokens = splitLine(line);

                    for (String token : tokens) {
                        tokenPairs.add(new Pair<>(token, lineNumber));
                    }
                }

                lineNumber++;
            }

            scanner.close();
            scanningAlgorithm(tokenPairs);
            writeResults();
        }
        catch (FileNotFoundException fe) {
            System.out.println("File not found");
        }
    }

    public void scanningAlgorithm(List<Pair<String, Integer>> tokens) {
        List<String> invalidTokens = new ArrayList<>();
        boolean isLexicallyCorrect = true;

        for (Pair<String, Integer> tokenPair: tokens) {
            String token = tokenPair.getKey();
            if (lang.isReservedWord(token) || lang.isOperator(token) || lang.isSeparator(token)) {
                pif.add(token, new Pair<>(-1, -1));
            }
            else if (lang.isIdentifier(token)) {
                identifiersSymbolTable.insertSymbolicName(token);
                Pair<Integer, Integer> position = identifiersSymbolTable.searchPosition(token);
                pif.add("id", position);
            }
            else if (lang.isConstant(token)) {
                constantsSymbolTable.insertSymbolicName(token);
                Pair<Integer, Integer> position = constantsSymbolTable.searchPosition(token);
                pif.add("const", position);
            }
            else if (!invalidTokens.contains(token)) {
                invalidTokens.add(token);
                isLexicallyCorrect = false;
                System.out.println("Lexical error at line " + tokenPair.getValue() + ", token: " + token + " not recognized");
            }
        }

        if (isLexicallyCorrect) {
            System.out.println("Program is lexically correct");
        }
    }

    public void writeResults() {
        try {
            File pifFile = new File(PIFFile);
            FileWriter fileWriter = new FileWriter(pifFile, false);
            fileWriter.write(pif.toString());
            fileWriter.close();

            File symbolTablesFile = new File(symbolTableFile);
            FileWriter fileWriterST = new FileWriter(symbolTablesFile, false);
            fileWriterST.write("Identifiers Table: \n" + identifiersSymbolTable.toString() + "\n\t---------------------------------------\n" + "Constants Table: \n" + constantsSymbolTable);
            fileWriterST.close();
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public List<String> splitLine(String line) {
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            if (lang.isSeparator(String.valueOf(line.charAt(i))) && !(String.valueOf(line.charAt(i))).equals(" ")) {
                tokens.add(String.valueOf(line.charAt(i)));
            }
            else if (line.charAt(i) == '`') {
                String stringConstant = identifyStringConstant(line, i);
                tokens.add(stringConstant);
                i += stringConstant.length() - 1;
            }
            else if (!(line.charAt(i) == '-' && Character.isDigit(line.charAt(i + 1))) && lang.isOperator(String.valueOf(line.charAt(i)))) {
                String operator = identifyOperator(line, i);
                tokens.add(operator);
                i += operator.length() - 1;
            }
            else if (line.charAt(i) == '-') {
                String minusToken = identifyNegativeNrToken(line, i);
                tokens.add(minusToken);
                i += minusToken.length() - 1;
            }
            else if (line.charAt(i) != ' ') {
                String token = identifyToken(line, i);
                tokens.add(token);
                i += token.length() - 1;
            }
        }

        return tokens;
    }

    public String identifyOperator(String line, int position) {
        String operator = "";
        for (int i = position; i < line.length(); i++) {
            char currentChar = line.charAt(i);
            operator += currentChar;

            // Check if the operator formed so far is a valid operator
            if (!lang.isOperator(operator)) {
                // The current operator is not valid, so consider the previous substring
                operator = operator.substring(0, operator.length() - 1);
                break;
            }
        }

        return operator;
    }

    public String identifyToken(String line, int position) {
        StringBuilder token = new StringBuilder();
        int i = position;
        while (i < line.length() && !lang.isSeparator(String.valueOf(line.charAt(i))) && line.charAt(i) != ' ') {
            token.append(line.charAt(i));
            i++;
        }

        return token.toString();
    }

    public String identifyStringConstant(String line, int position) {
        StringBuilder stringConstant = new StringBuilder();

        for (int i = position; i < line.length(); ++i) {
            if ((lang.isSeparator(String.valueOf(line.charAt(i))) || lang.isOperator(String.valueOf(line.charAt(i)))) && ((i == line.length() - 2 && line.charAt(i + 1) != '\"') || (i == line.length() - 1)))
                break;
            stringConstant.append(line.charAt(i));
            if (line.charAt(i) == '`' && i != position)
                break;
        }

        return stringConstant.toString();
    }

    public String identifyNegativeNrToken(String line, int position) {
        if (position < line.length() - 1 && Character.isDigit(line.charAt(position + 1))) {
            StringBuilder token = new StringBuilder();
            token.append('-');

            for (int i = position + 1; i < line.length() && (Character.isDigit(line.charAt(i))); i++) {
                token.append(line.charAt(i));
            }

            return token.toString();
        } else {
            return "-";
        }
    }

}
