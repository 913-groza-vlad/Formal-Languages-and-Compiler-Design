public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(16);

        System.out.println(symbolTable.insertSymbolicName("abc"));
        System.out.println(symbolTable.insertSymbolicName("cba"));
        System.out.println(symbolTable.insertSymbolicName("a"));
        System.out.println(symbolTable.insertSymbolicName("ab"));
        System.out.println(symbolTable.insertSymbolicName("zdam"));
        System.out.println(symbolTable.insertSymbolicName("ab"));
        System.out.println("---------------------------");

        System.out.println(symbolTable.containsSymbolicName("a"));
        System.out.println(symbolTable.containsSymbolicName("abc"));
        System.out.println(symbolTable.containsSymbolicName("cba"));
        System.out.println(symbolTable.containsSymbolicName("d"));
        System.out.println(symbolTable.containsSymbolicName("bca"));
        System.out.println("---------------------------");

        System.out.println(symbolTable.searchPosition("a"));
        System.out.println(symbolTable.searchPosition("abc"));
        System.out.println(symbolTable.searchPosition("cba"));
        System.out.println(symbolTable.searchPosition("zdam"));
        System.out.println(symbolTable.searchPosition("d"));

        System.out.println("---------------------------");

        System.out.println(symbolTable);
    }
}