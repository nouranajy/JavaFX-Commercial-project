package com.example.commercialjavaproject;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardController {

    @FXML private Button menuToggleBtn;
    @FXML private VBox sidebarContainer;
    @FXML private StackPane contentArea;
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

    private boolean isSidebarOpen = true;

    public void setUserData(String name, String email, String role) {
        headerUserNameLabel.setText(name);
        headerUserEmailLabel.setText(email);
        welcomeLabel.setText("Welcome back, " + name + "!");

        applyRolePermissions(role);

        // Automatically load the dashboard view when logging in
        showDashboard();
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

    // ================= SECTION SWITCHING LOGIC =================

    private void loadView(String fxmlFile, String breadcrumbText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/" + fxmlFile));
            Node view = loader.load();

            if (!contentArea.getChildren().isEmpty()) {
                Node oldContent = contentArea.getChildren().get(0);
                FadeTransition fadeOut = new FadeTransition(Duration.millis(150), oldContent);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(e -> {
                    contentArea.getChildren().clear();
                    contentArea.getChildren().add(view);
                    fadeInView(view);
                });
                fadeOut.play();
            } else {
                contentArea.getChildren().add(view);
                fadeInView(view);
            }

            breadcrumbLabel.setText(breadcrumbText);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fadeInView(Node view) {
        view.setOpacity(0.0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), view);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    @FXML
    public void showDashboard() {
        loadView("dashboard-home-view.fxml", "Home / Dashboard Overview");
    }

    @FXML
    public void showProducts() {
        loadView("products-view.fxml", "Home / Products Management");
    }

    @FXML
    public void showOrders() {
        loadView("orders-view.fxml", "Home / Orders Management");
    }

    @FXML
    public void showUsers() {
        loadView("users-view.fxml", "Home / User Management");
    }

    @FXML
    public void showSettings() {
        loadView("settings-view.fxml", "Home / Settings");
    }

    // ================= SIDEBAR & LOGOUT =================

    @FXML
    public void toggleSidebar() {
        // ... (Keep the exact same toggleSidebar code as before) ...
        Timeline timeline = new Timeline();
        KeyValue kv;

        if (isSidebarOpen) {
            kv = new KeyValue(sidebarContainer.prefWidthProperty(), 70);

            sidebarBrandLabel.setVisible(false);
            sidebarBrandLabel.setManaged(false);
            sidebarFooterLabel.setVisible(false);
            sidebarFooterLabel.setManaged(false);

            sidebarLogo.setFitWidth(25.0);
            sidebarLogo.setFitHeight(25.0);
            menuToggleBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-cursor: hand; -fx-text-fill: #FFFFFF; -fx-padding: 0 5 0 5;");

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

            sidebarBrandLabel.setVisible(true);
            sidebarBrandLabel.setManaged(true);
            sidebarFooterLabel.setVisible(true);
            sidebarFooterLabel.setManaged(true);

            sidebarLogo.setFitWidth(38.0);
            sidebarLogo.setFitHeight(38.0);
            menuToggleBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 20px; -fx-cursor: hand; -fx-text-fill: #FFFFFF; -fx-padding: 0;");

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
        // ... (Keep the exact same handleLogout code as before) ...
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), contentArea);
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