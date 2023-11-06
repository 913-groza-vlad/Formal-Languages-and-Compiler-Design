public class Main {
    public static void main(String[] args) {
//        SymbolTable symbolTable = new SymbolTable(20);

//        System.out.println(symbolTable.insertSymbolicName("abc"));
//        System.out.println(symbolTable.insertSymbolicName("cba"));
//        System.out.println(symbolTable.insertSymbolicName("a"));
//        System.out.println(symbolTable.insertSymbolicName("ab"));
//        System.out.println(symbolTable.insertSymbolicName("zdam"));
//        System.out.println(symbolTable.insertSymbolicName("ab"));
//        System.out.println("---------------------------");
//
//        System.out.println(symbolTable.containsSymbolicName("a"));
//        System.out.println(symbolTable.containsSymbolicName("abc"));
//        System.out.println(symbolTable.containsSymbolicName("cba"));
//        System.out.println(symbolTable.containsSymbolicName("d"));
//        System.out.println(symbolTable.containsSymbolicName("bca"));
//        System.out.println("---------------------------");
//
//        System.out.println(symbolTable.searchPosition("a"));
//        System.out.println(symbolTable.searchPosition("abc"));
//        System.out.println(symbolTable.searchPosition("cba"));
//        System.out.println(symbolTable.searchPosition("zdam"));
//        System.out.println(symbolTable.searchPosition("d"));
//
//        System.out.println("---------------------------");
//
//        System.out.println(symbolTable);

        System.out.println("Program 1");
        MyScanner scanner1 = new MyScanner("src/InputFiles/p1.txt", "src/OutputFiles/PIF1.txt", "src/OutputFiles/ST1.txt");
        scanner1.scan();

        System.out.println("\nProgram 2");
        MyScanner scanner2 = new MyScanner("src/InputFiles/p2.txt", "src/OutputFiles/PIF2.txt", "src/OutputFiles/ST2.txt");
        scanner2.scan();

        System.out.println("\nProgram 3");
        MyScanner scanner3 = new MyScanner("src/InputFiles/p3.txt", "src/OutputFiles/PIF3.txt", "src/OutputFiles/ST3.txt");
        scanner3.scan();

        System.out.println("\nProgram with errors");
        MyScanner errors_scanner = new MyScanner("src/InputFiles/p1err.txt", "src/OutputFiles/PIFerr.txt", "src/OutputFiles/STerr.txt");
        errors_scanner.scan();
    }
}