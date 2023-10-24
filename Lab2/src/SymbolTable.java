import java.util.ArrayList;
import java.util.List;

public class SymbolTable {

    private List<List<String>> table;
    private int size;

    public SymbolTable(int size) {
        this.table = new ArrayList<>();
        this.size = size;
        for (int i = 0; i < size; i++) {
            this.table.add(new ArrayList<>());
        }
    }

    private int hash(String key) {
        int asciiSum = 0;
        for (int i = 0; i < key.length(); i++) {
            asciiSum += key.charAt(i);
        }
        return asciiSum % this.size;
    }

    public boolean insertSymbolicName(String key) {
        int hashVal = hash(key);

        if (this.containsSymbolicName(key)) {
            return false;
        }

        this.table.get(hashVal).add(key);
        return true;
    }

    public boolean containsSymbolicName(String key) {
        int hashVal = hash(key);
        return table.get(hashVal).contains(key);
    }

    public Pair<Integer, Integer> searchPosition(String key) {
        if (this.containsSymbolicName(key)) {
            int listPosition = hash(key);
            List<String> list = table.get(listPosition);

            for (int listIndex = 0; listIndex < list.size(); listIndex++) {
                if (list.get(listIndex).equals(key)) {
                    return new Pair<>(listPosition, listIndex);
                }
            }
        }

        return new Pair<>(-1, -1);
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size; i++) {
            sb.append(i).append(": ");
            for (int j = 0; j < this.table.get(i).size(); j++) {
                sb.append(this.table.get(i).get(j)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
