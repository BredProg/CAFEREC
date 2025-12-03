package ph.edu.dlsu.lbycpei.caferecommmendationsystem;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CafeSystem {

    private Menu menu = new Menu();
    private RecommendationGraph graph = new RecommendationGraph();
    private Similarity similarity = new Similarity();
    private Order currentOrder = new Order();

    private Inventory inventory = new Inventory();
    public Inventory getInventory() { return inventory; }

    private Map<String, User> users = new HashMap<>();
    private User currentUser = null;

    public CafeSystem() {
        loadSampleMenu();
        loadUsers();
    }

    private void loadSampleMenu() {
        menu.addItem(new MenuItem("Matcha Latte", 150, "Hot Drinks"));
        menu.addItem(new MenuItem("Espresso", 120, "Hot Drinks"));
        menu.addItem(new MenuItem("Dubai Chocolate Brownie", 95, "Snacks"));
        menu.addItem(new MenuItem("Iced Tea", 100, "Cold Drinks"));
        menu.addItem(new MenuItem("Lemonade", 80, "Cold Drinks"));
    }

    private void loadSampleInventory() {
        inventory.addItem(new InventoryItem("Matcha Latte", 50, "Beverage"));
        inventory.addItem(new InventoryItem("Espresso", 30, "Beverage"));
        inventory.addItem(new InventoryItem("Dubai Chocolate Brownie", 100, "Sweet"));
        inventory.addItem(new InventoryItem("Iced Tea", 20, "Beverage"));
        inventory.addItem(new InventoryItem("Lemonade", 20, "Beverage"));
    }


    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            users = (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No saved users found or error loading users.");


        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password));
        saveUsers();
        return true;
    }

    public boolean loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logoutUser() {
        currentUser = null;
        startNewOrder();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void startNewOrder() {
        this.currentOrder = new Order();
    }

    public Order getCurrentOrder() {
        return this.currentOrder;
    }

    public boolean addItemToOrder(String itemName) {

        InventoryItem stock = inventory.getItemByName(itemName);
        if (stock == null) {
            System.out.println("Item does not exist in inventory.");
            return false;
        }

        if (stock.getQuantity() <= 0) {
            System.out.println("OUT OF STOCK: " + itemName);
            return false;
        }

        inventory.reduceStock(itemName);

        MenuItem item = menu.getItemByName(itemName);
        if (item != null) {
            currentOrder.addItem(item);
            return true;
        }

        return false;
    }

    public List<String> getRecommendations() {
        HashSet<String> recommendations = new HashSet<>();

        for (MenuItem item : currentOrder.getItems()) {
            String itemName = item.getName();

            String paired = graph.getTopRecommendation(itemName);
            if (paired != null
                    && menu.getItemByName(paired) != null
                    && !currentOrder.containsItem(paired)) {
                recommendations.add(paired);
            }

            var list = similarity.getSimilarItems(itemName);
            for (String sim : list) {
                if (menu.getItemByName(sim) != null
                        && !currentOrder.containsItem(sim)) {
                    recommendations.add(sim);
                }
            }
        }
        return new ArrayList<>(recommendations);
    }

    public boolean addRecommendedItem(String name) {
        MenuItem recItem = menu.getItemByName(name);
        if (recItem != null) {
            currentOrder.addItem(recItem);
            return true;
        }
        return false;
    }

    public String finalizeOrder() {
        if (!currentOrder.getItems().isEmpty()) {
            graph.updateGraph(currentOrder.getItems());

            if (currentUser != null) {
                currentUser.addOrderToHistory(currentOrder);
            }
        }

        String receiptText = Receipt.getReceiptText(currentOrder);

        startNewOrder();

        return receiptText;
    }

    public List<MenuItem> getMenuItems() {
        return menu.getItems();
    }
}
