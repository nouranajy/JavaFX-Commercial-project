package com.example.commercialjavaproject;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserFormController {

    @FXML private Label formTitle;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    private UsersController.UserModel userToEdit = null;
    private UsersController parentController;

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("ADMIN", "MANAGER", "CLIENT"));
    }

    public void setParentController(UsersController parentController) {
        this.parentController = parentController;
    }

    public void setUserToEdit(UsersController.UserModel user) {
        this.userToEdit = user;
        if (user != null) {
            formTitle.setText("Edit User: " + user.getFirstName());
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
            emailField.setText(user.getEmail());
            roleComboBox.setValue(user.getRoleName());
            passwordField.clear();
        }
    }

    @FXML
    public void generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        passwordField.setText(sb.toString());
    }

    @FXML
    public void copyPassword() {
        String pwd = passwordField.getText();
        if (pwd.isEmpty()) {
            showAlert("Warning", "Nothing to copy. Please generate a password first.");
            return;
        }

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(pwd);
        clipboard.setContent(content);

        showAlert("Copied!", "Password copied to clipboard:\n" + pwd);
    }

    @FXML
    public void cancelForm() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void saveUser() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String roleName = roleComboBox.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || roleName == null) {
            showAlert("Error", "Please fill in all required fields.");
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            int roleId = getRoleId(conn, roleName);

            if (userToEdit == null) {
                if (password.isEmpty()) {
                    showAlert("Error", "Password is required for new users.");
                    return;
                }
                String sql = "INSERT INTO users (role_id, first_name, last_name, email, password_hash) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, roleId);
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, email);
                pstmt.setString(5, PasswordUtils.hashPassword(password));
                pstmt.executeUpdate();
            } else {
                if (password.isEmpty()) {
                    String sql = "UPDATE users SET role_id=?, first_name=?, last_name=?, email=? WHERE user_id=?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, roleId);
                    pstmt.setString(2, firstName);
                    pstmt.setString(3, lastName);
                    pstmt.setString(4, email);
                    pstmt.setLong(5, userToEdit.getId());
                    pstmt.executeUpdate();
                } else {
                    String sql = "UPDATE users SET role_id=?, first_name=?, last_name=?, email=?, password_hash=? WHERE user_id=?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, roleId);
                    pstmt.setString(2, firstName);
                    pstmt.setString(3, lastName);
                    pstmt.setString(4, email);
                    pstmt.setString(5, PasswordUtils.hashPassword(password));
                    pstmt.setLong(6, userToEdit.getId());
                    pstmt.executeUpdate();
                }
            }

            if (parentController != null) parentController.refreshTable();
            cancelForm();

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            showAlert("Error", "Email already exists.");
        } catch (Exception e) {
            showAlert("Error", "Failed to save user: " + e.getMessage());
        }
    }

    private int getRoleId(Connection conn, String roleName) throws Exception {
        String sql = "SELECT role_id FROM roles WHERE role_name = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, roleName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) return rs.getInt("role_id");
        throw new Exception("Role not found");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}