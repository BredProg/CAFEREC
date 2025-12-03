package ph.edu.dlsu.lbycpei.caferecommmendationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class OrderViewController {

    private CafeSystem cafeSystem;

    @FXML private TableView<MenuItem> orderTable;
    @FXML private TableColumn<MenuItem, String> orderNameColumn;
    @FXML private TableColumn<MenuItem, Integer> orderPriceColumn;
    @FXML private Label totalLabel;


    public void setCafeSystem(CafeSystem system) {
        this.cafeSystem = system;
        initializeTable();
        refreshOrderTable();
        refreshTotal();
    }


    private void initializeTable() {
        orderNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        orderPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }


    public void refreshOrderTable() {
        if (cafeSystem != null) {
            orderTable.getItems().setAll(
                    cafeSystem.getCurrentOrder().getItems()
            );
        }
    }


    public void refreshTotal() {
        if (cafeSystem != null) {
            int total = 0;
            for (MenuItem item : cafeSystem.getCurrentOrder().getItems()) {
                total += item.getPrice();
            }
            totalLabel.setText("₱ " + total);
        }
    }


    @FXML
    private void handleCheckout() {
        String receipt = cafeSystem.finalizeOrder();
        System.out.println(receipt);

        refreshOrderTable();
        refreshTotal();
    }
}

