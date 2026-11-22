package com.example.commercialjavaproject;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HelloController {

    // ================= LOGIN UI =================
    @FXML private VBox loginContainer;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField; // The visible version
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    // ================= REGISTER UI =================
    @FXML private VBox registerContainer;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField regEmailField;
    @FXML private PasswordField regPasswordField;
    @FXML private TextField regPasswordVisibleField; // The visible version
    @FXML private PasswordField regConfirmPasswordField;
    @FXML private Label regErrorLabel;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        regErrorLabel.setVisible(false);

        // Sync the hidden and visible password fields for Login
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!passwordVisibleField.isVisible()) passwordVisibleField.setText(newVal);
        });
        passwordVisibleField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (passwordVisibleField.isVisible()) passwordField.setText(newVal);
        });

        // Sync the hidden and visible password fields for Register
        regPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!regPasswordVisibleField.isVisible()) regPasswordVisibleField.setText(newVal);
        });
        regPasswordVisibleField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (regPasswordVisibleField.isVisible()) regPasswordField.setText(newVal);
        });
    }

    // ================= PASSWORD TOGGLES =================

    @FXML
    public void toggleLoginPassword() {
        if (passwordField.isVisible()) {
            passwordVisibleField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordVisibleField.setVisible(true);
            passwordVisibleField.requestFocus();
            passwordVisibleField.positionCaret(passwordVisibleField.getText().length());
        } else {
            passwordField.setText(passwordVisibleField.getText());
            passwordVisibleField.setVisible(false);
            passwordField.setVisible(true);
            passwordField.requestFocus();
            passwordField.positionCaret(passwordField.getText().length());
        }
    }

    @FXML
    public void toggleRegPassword() {
        if (regPasswordField.isVisible()) {
            regPasswordVisibleField.setText(regPasswordField.getText());
            regPasswordField.setVisible(false);
            regPasswordVisibleField.setVisible(true);
            regPasswordVisibleField.requestFocus();
            regPasswordVisibleField.positionCaret(regPasswordVisibleField.getText().length());
        } else {
            regPasswordField.setText(regPasswordVisibleField.getText());
            regPasswordVisibleField.setVisible(false);
            regPasswordField.setVisible(true);
            regPasswordField.requestFocus();
            regPasswordField.positionCaret(regPasswordField.getText().length());
        }
    }

    // ================= ANIMATIONS & VIEW SWITCHING =================

    @FXML
    public void showRegisterForm() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), loginContainer);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            loginContainer.setVisible(false);
            registerContainer.setVisible(true);
            registerContainer.setOpacity(0.0);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), registerContainer);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    @FXML
    public void showLoginForm() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), registerContainer);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            registerContainer.setVisible(false);
            loginContainer.setVisible(true);
            loginContainer.setOpacity(0.0);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), loginContainer);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    // ================= LOGIN LOGIC =================

    @FXML
    public void handleLogin() {
        String email = emailField.getText().trim();
        // Get password from whichever field is currently visible
        String password = passwordField.isVisible() ? passwordField.getText().trim() : passwordVisibleField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError(errorLabel, "Please enter both email and password.");
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT u.password_hash, u.first_name, u.last_name, r.role_name " +
                    "FROM users u JOIN roles r ON u.role_id = r.role_id " +
                    "WHERE u.email = ? AND u.is_active = TRUE";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String roleName = rs.getString("role_name");

                if (PasswordUtils.checkPassword(password, storedHash)) {
                    openDashboard(firstName + " " + lastName, email, roleName);
                } else {
                    showError(errorLabel, "❌ Incorrect password.");
                }
            } else {
                showError(errorLabel, "❌ User not found or account is disabled.");
            }

        } catch (Exception e) {
            showError(errorLabel, "❌ Database error. Check console.");
            e.printStackTrace();
        }
    }

    // ================= REGISTER LOGIC =================

    @FXML
    public void handleRegister() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = regEmailField.getText().trim();
        String password = regPasswordField.isVisible() ? regPasswordField.getText().trim() : regPasswordVisibleField.getText().trim();
        String confirmPassword = regConfirmPasswordField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError(regErrorLabel, "Please fill in all fields.");
            return;
        }

        // CHECK IF PASSWORDS MATCH
        if (!password.equals(confirmPassword)) {
            showError(regErrorLabel, "❌ Passwords do not match. Please try again.");
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO users (role_id, first_name, last_name, email, password_hash) VALUES (3, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, PasswordUtils.hashPassword(password));

            pstmt.executeUpdate();

            System.out.println("✅ Client account created: " + email);

            firstNameField.clear();
            lastNameField.clear();
            regEmailField.clear();
            regPasswordField.clear();
            regPasswordVisibleField.clear();
            regConfirmPasswordField.clear();

            errorLabel.setText("✅ Account created! Please sign in.");
            errorLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            errorLabel.setVisible(true);

            showLoginForm();

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            showError(regErrorLabel, "❌ Email already exists. Please try another.");
        } catch (Exception e) {
            showError(regErrorLabel, "❌ Error creating account. Please try again.");
            e.printStackTrace();
        }
    }

    // ================= SMOOTH SCENE SWITCHING =================

    private void openDashboard(String fullName, String email, String roleName) {
        try {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), loginContainer);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
                    Parent root = loader.load();

                    DashboardController dashboardController = loader.getController();
                    dashboardController.setUserData(fullName, email, roleName);

                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    Scene scene = new Scene(root, 1100, 800);

                    root.setOpacity(0.0);
                    stage.setScene(scene);
                    stage.setTitle("Commercial App - Dashboard");
                    stage.centerOnScreen();

                    FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            fadeOut.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold;");
        label.setVisible(true);
    }
}