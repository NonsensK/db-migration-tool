package com.example.migration;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MigrationExecutor {
    private static final Logger logger = LoggerFactory.getLogger(MigrationExecutor.class);

    /**
     * Метод для выполнения миграции.
     * @param connection - подключение к базе данных
     * @param migrationFilePath - путь к файлу миграции (SQL)
     */

    //Метод executeMigration: Этот метод принимает подключение к базе данных и путь к файлу миграции.
    // Затем он читает содержимое SQL-файла и выполняет его на базе данных.
    public void executeMigration(Connection connection, String migrationFilePath) {

        //Метод readMigrationFile: Этот метод читает SQL-скрипт из файла и
        // по указанному пути и возвращает его содержимое как строку.
        String sql = readMigrationFile(migrationFilePath);
        if (sql == null) {
            logger.error("Не удалось прочитать файл миграции: " + migrationFilePath);
            return;
        }

        // Выполняем SQL-скрипт
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Миграция выполнена успешно: " + migrationFilePath);
        } catch (SQLException e) {
            logger.error("Ошибка при выполнении миграции: " + migrationFilePath, e);
        }
    }

    /**
     * Читает SQL-скрипт из файла.
     * @param filePath - путь к файлу
     * @return содержимое файла как строка
     */
    private String readMigrationFile(String filePath) {
        try {
            // Чтение файла и преобразование содержимого в строку
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла миграции: " + filePath, e);
            return null;
        }
    }
}
