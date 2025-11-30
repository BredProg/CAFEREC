package ph.edu.dlsu.lbycpei.caferecommmendationsystem;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<MenuItem> items = new ArrayList<>();

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Previous Order:\n: ");
        for (MenuItem item : items) {
            sb.append(item.getName()).append(", ");
        }
        return sb.toString();
    }

    public boolean containsItem(String name) {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
    }

}
