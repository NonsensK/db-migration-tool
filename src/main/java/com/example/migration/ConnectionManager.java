package com.example.migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConnectionManager {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public ConnectionManager() {
        // Загружаем параметры из application.properties
        loadProperties();
    }

    private void loadProperties() {
        try {
            // Загружаем application.properties
            Properties properties = new Properties();
            FileInputStream input = new FileInputStream("src/main/resources/application.properties");

            properties.load(input);

            // Получаем значения параметров
            dbUrl = properties.getProperty("db.url");
            dbUser = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");

            input.close();
        } catch (IOException e) {
            System.err.println("Ошибка загрузки файла application.properties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManager();

        try (Connection connection = connectionManager.getConnection()) {
            System.out.println("Соединение с базой данных успешно установлено!");
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }
}
