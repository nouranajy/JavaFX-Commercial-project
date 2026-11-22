package com.example.commercialjavaproject;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardController {

    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;
    @FXML private Label userRoleLabel;
    @FXML private Button logoutButton;

    // The root container of the dashboard, we need this to fade it out
    @FXML private VBox mainContentContainer;
    @FXML private VBox sidebarContainer;

    public void setUserData(String name, String email, String role) {
        userNameLabel.setText(name);
        userEmailLabel.setText(email);
        userRoleLabel.setText(role);
    }

    @FXML
    public void handleLogout() {
        // Fade out the dashboard before switching back to login
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), sidebarContainer);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        FadeTransition fadeOutMain = new FadeTransition(Duration.millis(300), mainContentContainer);
        fadeOutMain.setFromValue(1.0);
        fadeOutMain.setToValue(0.0);

        fadeOutMain.setOnFinished(e -> {
            try {
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 1100, 800);

                // Fade the new login scene IN
                root.setOpacity(0.0);
                stage.setScene(scene);
                stage.setTitle("Commercial App - Login");

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        fadeOut.play();
        fadeOutMain.play();
    }
}