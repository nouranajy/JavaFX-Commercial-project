package com.example.commercialjavaproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image; // <-- Added for the icon
import javafx.stage.Stage;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 1. Initialize DB and Test Connection
        DatabaseManager.initialize();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM roles")) {
            if (rs.next()) {
                System.out.println("✅ Connected to MySQL! Roles found: " + rs.getInt(1));
            }
        } catch (Exception e) {
            System.out.println("❌ DB connection failed!");
            e.printStackTrace();
        }

        // 2. Insert Admin from .env (Only runs if admin doesn't exist)
        createAdminFromEnv();

        // 3. Set Application Icon (Window & Taskbar)
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/commercialjavaproject/-main.png")));

        // 4. Show UI
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 800);
        stage.setTitle("NAJYGRID Enterprise - Login"); // <-- Updated Title
        stage.setScene(scene);
        stage.show();
    }

    private void createAdminFromEnv() {
        Dotenv dotenv = Dotenv.load();
        String adminEmail = dotenv.get("ADMIN_EMAIL");
        String adminPassword = dotenv.get("ADMIN_PASSWORD");

        if (adminEmail == null || adminPassword == null) {
            System.out.println("⚠️ Admin credentials not found in .env");
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM users WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, adminEmail);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("ℹ️ Admin user already exists in database.");
                return;
            }

            String insertSql = "INSERT INTO users (role_id, first_name, last_name, email, password_hash) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSql);
            pstmt.setInt(1, 1); // 1 = ADMIN role
            pstmt.setString(2, "System");
            pstmt.setString(3, "Owner");
            pstmt.setString(4, adminEmail);
            pstmt.setString(5, PasswordUtils.hashPassword(adminPassword));

            pstmt.executeUpdate();
            System.out.println("✅ Admin user created successfully from .env!");

        } catch (Exception e) {
            System.out.println("❌ Failed to insert admin.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

// Separated class for hashing logic
class PasswordUtils {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}