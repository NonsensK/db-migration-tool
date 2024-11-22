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
    public void executeMigration(Connection connection, String migrationFilePath) {
        // Чтение SQL-скрипта из файла миграции
        String sql = readMigrationFile(migrationFilePath);
        if (sql == null) {
            logger.error("Не удалось прочитать файл миграции: " + migrationFilePath);
            return;
        }

        // Выполнение SQL-скрипта
        try (Statement statement = connection.createStatement()) {
            // Выполнение миграции
            statement.executeUpdate(sql);
            logger.info("Миграция выполнена успешно: " + migrationFilePath);

            // Извлекаем версию миграции из пути файла
            String version = extractVersion(migrationFilePath);

            // Записываем выполненную миграцию в таблицу migration_history
            String insertHistorySql = "INSERT INTO migration_history (version) VALUES ('" + version + "')";
            statement.executeUpdate(insertHistorySql);
            logger.info("Миграция добавлена в историю: " + version);

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

    /**
     * Извлекает версию миграции из имени файла (например, V1 из V1__create_migration_history.sql).
     * @param migrationFilePath - путь к файлу миграции
     * @return версия миграции
     */
    private String extractVersion(String migrationFilePath) {
        // Извлекаем версию из имени файла миграции (например, V1, V2, V3 и т.д.)
        return migrationFilePath.split("__")[0].substring(1);
    }
}
