package ph.edu.dlsu.lbycpei.caferecommmendationsystem;

import java.util.HashMap;
import java.util.List;

public class RecommendationGraph {

    private HashMap<String, HashMap<String, Integer>> graph = new HashMap<>();

    public void updateGraph(List<MenuItem> orderItems) {
        for (int i = 0; i < orderItems.size(); i++) {
            for (int j = i + 1; j < orderItems.size(); j++) {

                String a = orderItems.get(i).getName();
                String b = orderItems.get(j).getName();

                graph.putIfAbsent(a, new HashMap<>());
                graph.putIfAbsent(b, new HashMap<>());

                graph.get(a).put(b, graph.get(a).getOrDefault(b, 0) + 1);
                graph.get(b).put(a, graph.get(b).getOrDefault(a, 0) + 1);
            }
        }
    }

    public String getTopRecommendation(String item) {
        if (!graph.containsKey(item)) return null;

        HashMap<String, Integer> neighbors = graph.get(item);
        String bestItem = null;
        int max = 0;

        for (String other : neighbors.keySet()) {
            int count = neighbors.get(other);
            if (count > max) {
                max = count;
                bestItem = other;
            }
        }

        return bestItem;
    }
}
