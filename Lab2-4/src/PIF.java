import java.util.ArrayList;
import java.util.List;

public class PIF {
    List<Pair<String, Pair<Integer, Integer>>> pif = new ArrayList<>();

    public void add(String token, Pair<Integer, Integer> position) {
        pif.add(new Pair<>(token, position));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Pair<String, Pair<Integer, Integer>> pair : pif) {
            stringBuilder.append(pair.getKey()).append(" | (").append(pair.getValue().getKey()).append(", ").append(pair.getValue().getValue()).append(")\n");
        }
        return stringBuilder.toString();
    }

}
