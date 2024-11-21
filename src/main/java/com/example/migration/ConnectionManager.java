package com.example.migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public ConnectionManager(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public static void main(String[] args) {
        // Убедитесь, что параметры совпадают с application.properties
        String dbUrl = "jdbc:postgresql://localhost:5432/migration_db";
        String dbUser = "postgres";
        String dbPassword = "password";

        ConnectionManager connectionManager = new ConnectionManager(dbUrl, dbUser, dbPassword);
        try (Connection connection = connectionManager.getConnection()) {
            System.out.println("Соединение с базой данных успешно установлено!");
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }
}
