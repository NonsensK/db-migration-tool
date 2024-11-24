package com.example.migration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public ConnectionManager() {
        // Загружаем параметры из application.properties
        loadProperties();
    }

    private void loadProperties() {
        try {
            // Загружаем application.properties из classpath
            Properties properties = new Properties();

            try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
                if (input == null) {
                    throw new IOException("Файл application.properties не найден");
                }
                properties.load(input);
            }

            // Получаем значения параметров
            dbUrl = properties.getProperty("db.url");
            dbUser = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");

        } catch (IOException e) {
            logger.error("Ошибка загрузки файла application.properties: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось загрузить параметры базы данных", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManager();

        try (Connection connection = connectionManager.getConnection()) {
            logger.info("Соединение с базой данных успешно установлено!");
        } catch (SQLException e) {
            logger.error("Ошибка подключения к базе данных: {}", e.getMessage(), e);
        }
    }
}
