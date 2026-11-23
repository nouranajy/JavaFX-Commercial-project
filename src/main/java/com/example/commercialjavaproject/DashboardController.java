package com.example.commercialjavaproject;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardController {

    @FXML private Button menuToggleBtn;
    @FXML private VBox sidebarContainer;
    @FXML private VBox mainContentContainer;

    // Added Logo and brand labels
    @FXML private ImageView sidebarLogo;
    @FXML private Label sidebarBrandLabel;
    @FXML private Label sidebarFooterLabel;

    @FXML private Label headerUserNameLabel;
    @FXML private Label headerUserEmailLabel;
    @FXML private Button logoutButton;

    @FXML private Button dashboardBtn;
    @FXML private Button productsBtn;
    @FXML private Button ordersBtn;
    @FXML private Button usersBtn;
    @FXML private Button settingsBtn;

    @FXML private Label welcomeLabel;
    @FXML private Label breadcrumbLabel;
    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;
    @FXML private Label userRoleLabel;

    private boolean isSidebarOpen = true;

    public void setUserData(String name, String email, String role) {
        headerUserNameLabel.setText(name);
        headerUserEmailLabel.setText(email);
        userNameLabel.setText(name);
        userEmailLabel.setText(email);
        userRoleLabel.setText(role);

        welcomeLabel.setText("Welcome back, " + name + "!");
        applyRolePermissions(role);
    }

    private void applyRolePermissions(String role) {
        productsBtn.setVisible(false); productsBtn.setManaged(false);
        ordersBtn.setVisible(false); ordersBtn.setManaged(false);
        usersBtn.setVisible(false); usersBtn.setManaged(false);
        settingsBtn.setVisible(false); settingsBtn.setManaged(false);

        if (role.equals("ADMIN")) {
            productsBtn.setVisible(true); productsBtn.setManaged(true);
            ordersBtn.setVisible(true); ordersBtn.setManaged(true);
            usersBtn.setVisible(true); usersBtn.setManaged(true);
            settingsBtn.setVisible(true); settingsBtn.setManaged(true);
            breadcrumbLabel.setText("Home / Admin Dashboard Overview");
        } else if (role.equals("MANAGER")) {
            productsBtn.setVisible(true); productsBtn.setManaged(true);
            ordersBtn.setVisible(true); ordersBtn.setManaged(true);
            settingsBtn.setVisible(true); settingsBtn.setManaged(true);
            breadcrumbLabel.setText("Home / Manager Dashboard Overview");
        } else if (role.equals("CLIENT")) {
            breadcrumbLabel.setText("Home / Client Store Overview");
        }
    }

    @FXML
    public void toggleSidebar() {
        Timeline timeline = new Timeline();
        KeyValue kv;

        if (isSidebarOpen) {
            kv = new KeyValue(sidebarContainer.prefWidthProperty(), 70);

            // Hide text
            sidebarBrandLabel.setVisible(false);
            sidebarBrandLabel.setManaged(false);
            sidebarFooterLabel.setVisible(false);
            sidebarFooterLabel.setManaged(false);

            // Shrink Logo & Adjust padding to center hamburger and logo
            sidebarLogo.setFitWidth(25.0);
            sidebarLogo.setFitHeight(25.0);
            menuToggleBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-cursor: hand; -fx-text-fill: #FFFFFF; -fx-padding: 0 5 0 5;");

            // Collapse buttons
            dashboardBtn.setText("📊");
            dashboardBtn.setStyle("-fx-background-color: #1A3C5A; -fx-text-fill: #FFFFFF; -fx-alignment: CENTER; -fx-padding: 0; -fx-background-radius: 8; -fx-font-size: 18px; -fx-cursor: hand;");

            productsBtn.setText("📦");
            productsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-alignment: CENTER; -fx-padding: 0; -fx-background-radius: 8; -fx-font-size: 18px; -fx-cursor: hand;");

            ordersBtn.setText("🛒");
            ordersBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-alignment: CENTER; -fx-padding: 0; -fx-background-radius: 8; -fx-font-size: 18px; -fx-cursor: hand;");

            usersBtn.setText("👥");
            usersBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-alignment: CENTER; -fx-padding: 0; -fx-background-radius: 8; -fx-font-size: 18px; -fx-cursor: hand;");

            settingsBtn.setText("⚙️");
            settingsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-alignment: CENTER; -fx-padding: 0; -fx-background-radius: 8; -fx-font-size: 18px; -fx-cursor: hand;");

            logoutButton.setText("⏻");
            logoutButton.setPrefWidth(40.0);
            logoutButton.setStyle("-fx-background-color: transparent; -fx-border-color: #334E68; -fx-border-radius: 6; -fx-text-fill: #A0B3C6; -fx-font-size: 18px; -fx-cursor: hand; -fx-alignment: CENTER;");

        } else {
            kv = new KeyValue(sidebarContainer.prefWidthProperty(), 250);

            // Show text
            sidebarBrandLabel.setVisible(true);
            sidebarBrandLabel.setManaged(true);
            sidebarFooterLabel.setVisible(true);
            sidebarFooterLabel.setManaged(true);

            // Restore Logo & padding
            sidebarLogo.setFitWidth(38.0);
            sidebarLogo.setFitHeight(38.0);
            menuToggleBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 20px; -fx-cursor: hand; -fx-text-fill: #FFFFFF; -fx-padding: 0;");

            // Expand buttons
            dashboardBtn.setText("📊   Dashboard");
            dashboardBtn.setStyle("-fx-background-color: #1A3C5A; -fx-text-fill: #FFFFFF; -fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 15; -fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");

            productsBtn.setText("📦   Products");
            productsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 15; -fx-background-radius: 8; -fx-font-size: 14px; -fx-cursor: hand;");

            ordersBtn.setText("🛒   Orders");
            ordersBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 15; -fx-background-radius: 8; -fx-font-size: 14px; -fx-cursor: hand;");

            usersBtn.setText("👥   Manage Users");
            usersBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 15; -fx-background-radius: 8; -fx-font-size: 14px; -fx-cursor: hand;");

            settingsBtn.setText("⚙️   Settings");
            settingsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 15; -fx-background-radius: 8; -fx-font-size: 14px; -fx-cursor: hand;");

            logoutButton.setText("Sign out");
            logoutButton.setPrefWidth(220.0);
            logoutButton.setStyle("-fx-background-color: transparent; -fx-border-color: #334E68; -fx-border-radius: 6; -fx-text-fill: #A0B3C6; -fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand; -fx-alignment: CENTER;");
        }

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(250), kv));
        timeline.play();
        isSidebarOpen = !isSidebarOpen;
    }

    @FXML
    public void handleLogout() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), mainContentContainer);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            try {
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = loader.load();

                double currentWidth = stage.getWidth();
                double currentHeight = stage.getHeight();
                Scene scene = new Scene(root, currentWidth, currentHeight);

                root.setOpacity(0.0);
                stage.setScene(scene);
                stage.setTitle("NAJYGRID Enterprise - Login");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/commercialjavaproject/logo.png")));

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        fadeOut.play();
    }
}