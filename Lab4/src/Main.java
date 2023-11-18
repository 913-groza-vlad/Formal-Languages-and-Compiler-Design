import java.util.Scanner;

public class Main {
    public static void printMenu() {
        System.out.println("\n1. Display states.");
        System.out.println("2. Display alphabet.");
        System.out.println("3. Display transitions.");
        System.out.println("4. Display initial state.");
        System.out.println("5. Display final states.");
        System.out.println("6. Verify if a sequence is accepted by the FA.\n");
    }

    public static void main(String[] args) {
        System.out.println("Program 1");
        MyScanner program1Scanner = new MyScanner("src/Files/p1.txt", "src/OutputFiles/PIF1.txt", "src/OutputFiles/ST1.txt");
        program1Scanner.scan();

        System.out.println("\nProgram 2");
        MyScanner program2Scanner = new MyScanner("src/Files/p2.txt", "src/OutputFiles/PIF2.txt", "src/OutputFiles/ST2.txt");
        program2Scanner.scan();

        System.out.println("\nProgram 3");
        MyScanner program3Scanner = new MyScanner("src/Files/p3.txt", "src/OutputFiles/PIF3.txt", "src/OutputFiles/ST3.txt");
        program3Scanner.scan();

        System.out.println("\nProgram with errors");
        MyScanner errors_scanner = new MyScanner("src/Files/p1err.txt", "src/OutputFiles/PIFerr.txt", "src/OutputFiles/STerr.txt");
        errors_scanner.scan();

        FiniteAutomaton fa = new FiniteAutomaton("src/Files/FA.in");
        fa.readFAFromFile();
        System.out.println("\nFA was read from file\n" + fa);

        printMenu();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose an option: ");
        int option = scanner.nextInt();
        while (option != 0) {
            switch (option) {
                case 1 ->
                    System.out.println(fa.statesToString(fa.getStates(), "States"));
                case 2 ->
                    System.out.println(fa.alphabetToString());
                case 3 ->
                    System.out.println(fa.transitionsToString());
                case 4 ->
                    System.out.println("Initial state: " + fa.getInitialState());
                case 5 ->
                    System.out.println(fa.statesToString(fa.getFinalStates(), "Final states"));
                case 6 -> {
                    if (fa.isDFA()) {
                        System.out.print("Enter a sequence: ");
                        Scanner scanner1 = new Scanner(System.in);
                        String sequence = scanner1.nextLine();
                        if (fa.isSequenceAccepted(sequence))
                            System.out.println("Sequence is accepted by the FA");
                        else
                            System.out.println("Sequence is not accepted by the FA");
                    }
                    else
                        System.out.println("FA is not a DFA");
                }

            }

            printMenu();
            System.out.print("Choose an option: ");
            option = scanner.nextInt();
        }
    }
}