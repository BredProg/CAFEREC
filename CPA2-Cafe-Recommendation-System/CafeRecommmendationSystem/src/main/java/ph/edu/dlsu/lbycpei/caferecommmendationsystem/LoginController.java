package ph.edu.dlsu.lbycpei.caferecommmendationsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private CafeSystem cafeSystem;

    public void setCafeSystem(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (cafeSystem.loginUser(username, password)) {
            goToMainScreen(username);
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void handleGoToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register-view.fxml"));
            Parent root = loader.load();

            RegisterController controller = loader.getController();
            controller.setCafeSystem(cafeSystem);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 300));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initialize() {
    }

    private void goToMainScreen(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cafe-view.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setCafeSystem(cafeSystem);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setTitle("Cafe Ordering - Logged in as " + username);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Error loading the main screen.");
        }
    }

}
