//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ph.edu.dlsu.lbycpei.caferecommmendationsystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class CafeSystem {
    private Menu menu = new Menu();
    private RecommendationGraph graph = new RecommendationGraph();
    private Similarity similarity = new Similarity();
    private Scanner sc;

    public CafeSystem() {
        this.sc = new Scanner(System.in);
        this.loadSampleMenu();
    }

    private void loadSampleMenu() {
        this.menu.addItem(new MenuItem("Matcha Latte", (double)150.0F));
        this.menu.addItem(new MenuItem("Espresso", (double)120.0F));
        this.menu.addItem(new MenuItem("Dubai Chocolate Brownie", (double)95.0F));
        this.menu.addItem(new MenuItem("Iced Tea", (double)100.0F));
        this.menu.addItem(new MenuItem("Lemonade", (double)80.0F));
    }

    public void run() {
        while(true) {
            System.out.println("\n1. View Menu");
            System.out.println("2. Place Order");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            int choice = this.sc.nextInt();
            this.sc.nextLine();
            switch (choice) {
                case 1:
                    this.menu.displayMenu();
                    break;
                case 2:
                    this.placeOrder();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
            }
        }
    }

    private void placeOrder() {
        Order order = new Order();
        this.menu.displayMenu();

        while(true) {
            System.out.print("Enter item name (or 'done'): ");
            String input = this.sc.nextLine();
            if (input.equalsIgnoreCase("done")) {
                this.graph.updateGraph(order.getItems());
                this.addRecommendedItems(order);
                Receipt.printReceipt(order);
                return;
            }

            MenuItem item = this.menu.getItemByName(input);
            if (item != null) {
                order.addItem(item);
                System.out.println("Added: " + item.getName());
            } else {
                System.out.println("Item not found.");
            }
        }
    }

    private void addRecommendedItems(Order order) {
        while(true) {
            System.out.println("\n--- Recommended Items ---");
            HashSet<String> recommendations = new HashSet();

            for(MenuItem item : order.getItems()) {
                String itemName = item.getName();
                String paired = this.graph.getTopRecommendation(itemName);
                if (paired != null && this.menu.getItemByName(paired) != null && !order.containsItem(paired)) {
                    recommendations.add(paired);
                }

                for(String sim : this.similarity.getSimilarItems(itemName)) {
                    if (this.menu.getItemByName(sim) != null && !order.containsItem(sim)) {
                        recommendations.add(sim);
                    }
                }
            }

            if (recommendations.isEmpty()) {
                System.out.println("No recommendations available.");
                return;
            }

            int index = 1;
            ArrayList<String> recList = new ArrayList(recommendations);

            for(String r : recList) {
                System.out.println(index + ". " + r);
                ++index;
            }

            System.out.print("Would you like to add any recommended item? (name or 'done'): ");
            String input = this.sc.nextLine();
            if (input.equalsIgnoreCase("done")) {
                return;
            }

            MenuItem recItem = this.menu.getItemByName(input);
            if (recItem != null && recList.contains(recItem.getName())) {
                order.addItem(recItem);
                System.out.println("Added: " + recItem.getName());
                this.graph.updateGraph(order.getItems());
            } else {
                System.out.println("Invalid recommendation.");
            }
        }
    }
}
