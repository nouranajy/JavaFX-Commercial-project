package com.example.commercialjavaproject;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private static HikariDataSource dataSource;

    public static void initialize() {
        if (dataSource != null) return;

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String host = dotenv.get("DB_HOST", "");
        String port = dotenv.get("DB_PORT", "");
        String user = dotenv.get("DB_USER", "");
        String password = dotenv.get("DB_PASSWORD", "");
        String dbName = dotenv.get("DB_NAME", "");

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setPoolName("CommercialAppPool");

        dataSource = new HikariDataSource(config);
        System.out.println("✅ Database Pool Initialized!");
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) initialize();
        return dataSource.getConnection();
    }
}